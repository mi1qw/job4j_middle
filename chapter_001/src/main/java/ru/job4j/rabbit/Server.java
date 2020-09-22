package ru.job4j.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Server {
    public static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static final String BEGINREG = "^GET /\\?msg=";
    private static final String ENDNREG = " HTTP.*\\z";
    private static final ExecutorService EXEC = Executors.newCachedThreadPool();

    /**
     * Start.
     */
    public void start() {
        try (ServerSocket server = new ServerSocket(9000)) {
            while (!EXEC.isShutdown()) {
                final Socket connection = server.accept();
                EXEC.execute(() -> handleRequest(connection));
                //EXEC.shutdown();
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Stop.
     */
    public void stop() {
        EXEC.shutdown();
    }

    private void handleRequest(final Socket conn) {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
             //BufferedWriter out = new BufferedWriter(
             //        new OutputStreamWriter(conn.getOutputStream()))
             OutputStream out =
                     conn.getOutputStream()
        ) {
            String str = in.readLine();
            //System.out.println(in.readLine());
            //while ((str = in.readLine()) != null) {
            System.out.println(getMesage(str));
            //    //System.out.println(str);
            //}
            //EXEC.shutdown();
            //out.write("HTTP/1.1 qqqq 200 OK\\r\\n\\r\\n".getBytes());
            out.write("HTTP/1.1 200 OK\r\n".getBytes());
            out.flush();
            //System.out.println("11111111");
            new Exchange("queueName", Rabbit.ExchangeType.FANOUT);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private String getMesage(final String mesage) {
        String str = mesage.replaceAll(BEGINREG, "");
        return str.replaceAll(ENDNREG, "");
    }
}

//channel.exchangeDeclare(EXCHANGE_NAME, "topic");
// = channel.queueDeclare().getQueue();
//        for (String bindingKey : argv) {
//        channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
//        }


class Exchange implements InQueue {
    private final String queueName;
    private final Rabbit.ExchangeType queueType;
    private final Map<String, InnerQueue> queue = new ConcurrentHashMap<>();
    private final Exchangemethods type;

    Exchange(final String queueName, final Rabbit.ExchangeType queueType) {
        this.queueName = queueName;
        this.queueType = queueType;
        this.type = queueType.getMethods();
    }

    /**
     * queueBind.
     *
     * @param bindingKey queueBind
     */
    @Override
    public void queueBind(final String bindingKey) {
        //queue.add(new InnerQueue(bindingKey));
        //type.
        queue.put(bindingKey, new InnerQueue(bindingKey));
    }

    /**
     * add.
     *
     * @param message
     * @return
     */
    @Override
    public String add(final String message) {
        type.route(message);
        return null;
    }

    /**
     * get.
     */
    @Override
    public String get() {
        return null;
    }

    void topic() {

    }

    void queue() {

    }

    protected final class InnerQueue {
        private final String bindingKey;
        private final ConcurrentLinkedQueue<String> innerqueue1;

        private InnerQueue(final String bindingKey) {
            this.bindingKey = bindingKey;
            this.innerqueue1 = new ConcurrentLinkedQueue<>();
        }

        private String getBindingKey() {
            return bindingKey;
        }

        private ConcurrentLinkedQueue<String> getInnerqueue1() {
            return innerqueue1;
        }
    }
}


interface InQueue {
    String add(String message);

    String get();

    void queueBind(String queueBind);
}


interface Exchangemethods {
    Exchange.InnerQueue route(String key);

    String add(String message);

    String get();
}


class Topic implements Exchangemethods {
    @Override
    public Exchange.InnerQueue route(final String key) {
        String[] str = key.split("\\.");
        String str1 = Arrays.stream(str).map(n -> n
                .replaceAll("^[*]$", "^\\\\\\\\w+\\$")
                .replaceAll("^[#]$", ".+"))
                //.replaceAll("^\\w+$", "A"))
                .collect(Collectors.joining("\\\\."));
        //.toArray(String[]::new);
        System.out.println(str1);
        System.out.println(Arrays.toString(str));
        System.out.println(str.length);
        //str.length > 1 &&

        return null;
    }

    @Override
    public String add(final String message) {
        return null;
    }

    @Override
    public String get() {
        return null;
    }
}


class Direct implements Exchangemethods {
    @Override
    public Exchange.InnerQueue route(final String key) {
        return null;
    }

    @Override
    public String add(final String message) {
        return null;
    }

    @Override
    public String get() {
        return null;
    }
}


class Comp {
    private int n1;
    private int n2;
    private boolean res = false;

    Comp(final int n1, final int n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    public int getN1() {
        return n1;
    }

    public int getN2() {
        return n2;
    }

    public boolean isRes() {
        return res;
    }
}

//aa.#.bb
//aa.11.22.33.bb


class CompareMask {
    private static int c3;
    private static int p3;
    private static int l1;
    private static int l2;
    private static String[] c1;
    private static String[] p2;
    private static boolean res = true;

    static boolean compare(final String[] c11, final String[] p22) {
        c1 = c11;
        p2 = p22;
        c3 = 0;
        p3 = 0;
        l1 = c1.length;
        l2 = p2.length;
        String c;
        String p;

        //for (int i = 0; i < l1; i++) {
        do {
            c = c1[c3];
            p = p2[p3];
            if (p.equals("#")) {
                System.out.println("#");
                manyWord();
                //} else if (p.equals("*")) {
                //    System.out.println("*");
            } else if (c.equals(p) || p.equals("*")) {
                c3++;
                p3++;
            } else {
                res = false;
                break;
            }
        } while (res && c3 < l1);
        //return new Comp(1, 1);
        return res;
    }

    static void manyWord() {
        int c4 = c3 + 1;
        int p4 = p3 + 1;
        if (p4 == l2) {
            //res = true;
            return;
        }

        while (c4 < l1) {
            if (!c1[c4].equals(p2[p4])) {
                c4++;
            } else {
                System.out.println(p4 + "=" + p2[p4] + "   " + c4 + "=" + c1[c4]);
                c3 = c4;
                p3 = p4;
                break;
            }
        }
    }
}

//class Comp {
//    private String[] c1;
//    private String[] c2;
//    private int n1;
//    private int n2;
//
//    Comp(final String[] c1, final String[] c2) {
//        this.c1 = c1;
//        this.c2 = c2;
//        this.n1 = n1;
//        this.n2 = n2;
//    }
//
//    public String[] getC1() {
//        return c1;
//    }
//
//    public String[] getC2() {
//        return c2;
//    }
//}






