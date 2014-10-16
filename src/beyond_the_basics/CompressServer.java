package beyond_the_basics;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class CompressServer {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Parameter(s): <Port>");
        }

        int port = Integer.parseInt(args[0]);
        System.out.println("Server  is listening at port " + port);

        ServerSocket serverSocket = new ServerSocket(port);
        Logger logger = Logger.getLogger("practical");
        Executor executor = Executors.newCachedThreadPool();

        // run forever and dispatch request
        while (true) {
            Socket clnSocket = serverSocket.accept();
            executor.execute(new CompressProtocol(clnSocket, logger));
        }
    }
}
