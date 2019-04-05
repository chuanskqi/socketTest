package cn.sk.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server {
    private static class SocketClient implements Runnable {
        private Socket socket;

        private Scanner scanner;

        private PrintWriter printWriter;
        public SocketClient(Socket socket) throws Exception{
            this.socket = socket;
            this.scanner = new Scanner(socket.getInputStream());
            this.scanner.useDelimiter("\n");
            this.printWriter = new PrintWriter(socket.getOutputStream());
        }

        public void run() {
            boolean flag = true;
            while (flag) {
                if (scanner.hasNext()) {
                    String value = scanner.next().trim();
                    if ("end".equalsIgnoreCase(value)) {
                        printWriter.println("服务器端关闭!");
                        printWriter.flush();
                        flag = false;
                    } else {
                        printWriter.println("Echo:" + value);
                        printWriter.flush();
                    }
                }
            }
            System.out.println("服务器端关闭当前客户端线程>>>" + Thread.currentThread().getName());
            try {
                socket.close();
                scanner.close();
                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("服务器端已经启动!");
        ExecutorService executorService = Executors.newCachedThreadPool();
        boolean flag = true;
        while (flag) {
            Socket socket = serverSocket.accept();
            System.out.println("接收到客户端");
            executorService.execute(new SocketClient(socket));
        }
        serverSocket.close();
        executorService.shutdown();
    }

}
