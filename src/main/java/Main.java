import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    static int port = 8989;

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine();
        System.out.println(engine.search("бизнес"));


        try (ServerSocket serverSocket = new ServerSocket(port)) {

            BooleanSearchEngine booleanSearchEngine = new BooleanSearchEngine();
            Gson gson = new GsonBuilder().create();

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String word = in.readLine();
                    List<PageEntry> page = booleanSearchEngine.search(word);

                    if (page == null) {
                        out.println("Совпадения отсутствуют");
                    }

                    var json = gson.toJson(page);
                    out.println(json);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}