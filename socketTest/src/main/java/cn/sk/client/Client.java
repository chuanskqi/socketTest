package cn.sk.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class Client {

    private static BufferedReader KB = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("127.0.0.1", 9999);
        Scanner scanner = new Scanner(socket.getInputStream()); //输入
        scanner.useDelimiter("\n");
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());    //输出

        boolean flag = true;

        while (flag) {
            String value = getInput("请输入数据").trim();
            printWriter.println(value);
            printWriter.flush();
            if (scanner.hasNext()) {
                System.out.println("接收到服务端的回应>>>>" + scanner.next());
            }

            if("end".equalsIgnoreCase(value)){
                System.out.println("客户端程序结束");
                flag = false;
            }
        }
        scanner.close();
        printWriter.close();
        socket.close();

    }

    public static String getInput(String prompt) throws Exception {
        System.out.println(prompt);
        return KB.readLine();
    }
}
