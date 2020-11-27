package ru.job4j.videocameradata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.stream.Stream;

public class Videocameradata {
    public static final Logger LOGGER = LoggerFactory.getLogger(Videocameradata.class);
    private ConcurrentMap<Integer, Camera> cameraList;
    private static String urlCameras = "http://www.mocky.io/v2/5c51b9dd3400003252129fb5";
    private ScheduledExecutorService exec;
    private ExecutorService execST;

    public Videocameradata() {
        cameraList = new ConcurrentHashMap<>();
        exec = Executors.newScheduledThreadPool(1);
        execST = Executors.newCachedThreadPool();
    }

    /**
     * Gets all cameras list.
     *
     * @return the camera list
     */
    public ConcurrentMap<Integer, Camera> getCameraList() {
        return cameraList;
    }

    /**
     * Start.
     */
    public void start() {
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
    }

    /**
     * Stop.
     */
    public void stop() {
        exec.shutdown();
        execST.shutdown();
        while (!execST.isTerminated()) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
