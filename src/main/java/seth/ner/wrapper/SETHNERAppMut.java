package seth.ner.wrapper;

import de.hu.berlin.wbi.objects.MutationMention;
import seth.SETH;
import edu.uchsc.ccp.nlp.ei.mutation.MutationFinder;
import java.util.List;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.lang.StringBuilder;

/**
* Apply SETH (including MutationFinder) on free text, either as simple call-once script or as a simple server.
*/

public class SETHNERAppMut {

  static final SETHNERAppMut singleton = new SETHNERAppMut();

  SETH seth = null;
  Boolean useMutationFinderOnly = DEFAULT_USE_MF_ONLY;

  //-----------------------------------------------------------------------------------------------

  public static final boolean DEFAULT_USE_MF_ONLY = false;

  //-----------------------------------------------------------------------------------------------

  public static void main(String[] args) throws Exception {

    if (args[0].equals("-p")) {
      int port = Integer.parseInt(args[1]);
      System.err.println("Starting server on port: " + port);

      singleton.useMutationFinderOnly = DEFAULT_USE_MF_ONLY;
      MutationFinder mf = getMF(singleton.useMutationFinderOnly);
      singleton.seth = new SETH(mf, true, true);

      HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
      server.createContext("/", new MyHandler());
      server.setExecutor(null); // creates a default executor
      server.start();
    }
    else {
      String text = args[0];
      singleton.useMutationFinderOnly = (args.length > 1) ? Boolean.parseBoolean(args[1]) : DEFAULT_USE_MF_ONLY;
      MutationFinder mf = getMF(singleton.useMutationFinderOnly);
      singleton.seth = new SETH(mf, true, true);
      System.out.println(singleton.apply(text));
    }

  }

  public static MutationFinder getMF(Boolean useMutationFinderOnly) {
    //MutationFinder mf = (singleton.useMutationFinderOnly) ? MutationFinder.useOriginalRegex() : new MutationFinder();
    //With the original regex we get the error: Input residue not recognized, must be a standard residue: ' ' -->
    MutationFinder mf = new MutationFinder();
    return mf;
  }

  public String apply(String text) {
    List<MutationMention> ret = (this.useMutationFinderOnly) ? this.seth.findMutationsWithMutationFinder(text) : this.seth.findMutations(text);
    return toString(ret);
  }

  public String toString(List<MutationMention> mutations) {
    StringBuilder sb = new StringBuilder();
    for (MutationMention mutation : mutations) {
      sb.append(mutation.toBratFormat());
      sb.append("\n");
    }
    return sb.toString();
  }

  static class MyHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
      //Assumed sole parameter: text=
      String param = "text=";

      String text = t.getRequestURI().getRawQuery();
      text = text.substring(param.length());
      text = java.net.URLDecoder.decode(text, "UTF-8");

      System.err.println(text);

      String response = singleton.apply(text);
      System.out.println(response);

      t.sendResponseHeaders(200, response.length());
      OutputStream os = t.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
  }

}
