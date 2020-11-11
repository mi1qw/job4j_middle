package ru.job4j.videocameradata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.stream.Stream;

public class Videocameradata {
    public static final Logger LOGGER = LoggerFactory.getLogger(Videocameradata.class);
    private static ConcurrentMap<Integer, Camera> cameraList = new ConcurrentHashMap<>();
    private static String urlCameras = "http://www.mocky.io/v2/5c51b9dd3400003252129fb5";

    public static void main(final String[] args) throws InterruptedException {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        ExecutorService execST = Executors.newCachedThreadPool();
        Runnable task = () -> {
            DataAndURL dau = new DataAndURL();
            DataURLCameras[] list = dau.getlist(dau.strToURL(urlCameras));
            Stream.of(list).forEach(n -> {
                CompletableFuture<SourceDataUrl> source = CompletableFuture.supplyAsync(() -> {
                            DataAndURL dau1 = new DataAndURL();
                            return dau1.get(n.getSourceDataUrl(), SourceDataUrl.class);
                        }, execST
                );
                CompletableFuture<TokenDataUrl> token = CompletableFuture.supplyAsync(() -> {
                            DataAndURL dau1 = new DataAndURL();
                            return dau1.get(n.getTokenDataUrl(), TokenDataUrl.class);
                        }, execST
                );
                source.thenCombine(token,
                        (s, t) -> {
                            try {
                                cameraList.put(n.getId(),
                                        new Camera(n.getId(),
                                                source.get(), token.get()));
                            } catch (InterruptedException | ExecutionException e) {
                                Thread.currentThread().interrupt();
                                LOGGER.error(e.getMessage(), e);
                            }
                            return null;
                        });
            });
        };
        exec.scheduleAtFixedRate(
                task,
                0,
                2700,
                TimeUnit.MILLISECONDS
        );
        Thread.sleep(20000);
        exec.shutdown();
        execST.shutdown();
        while (!execST.isTerminated()) {
            Thread.sleep(10);
        }
        cameraList.forEach((id, cam) -> System.out.println(cam));
    }
}
