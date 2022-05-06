import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;

import com.sun.net.httpserver.HttpServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.regex.Pattern;

import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import static java.util.concurrent.TimeUnit.*;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;





class Application {
	
	
	
	static Reset reset = new Reset();
	
    public static void main(String[] args) throws IOException {
        int serverPort = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
      
        server.createContext("/api/smaato/accept", (exchange -> {

            if ("GET".equals(exchange.getRequestMethod())) {
            	String respText;
            	
            	try {
            	Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());
            	
            	String noEndpoint = "default";
            	String endpoint = params.getOrDefault("endpoint", List.of(noEndpoint)).stream().findFirst().orElse(noEndpoint);
            	
            	
        		int id = Integer.valueOf(params.get("id").get(0));
        		
        		try {
					reset.addId(id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	respText = String.format("ok");
            	if(!endpoint.equals("default")) {
            		urlGET(endpoint);
            	}
            	
            	} catch (Exception e) {
            		respText = String.format("failed");
            	}
            	
            	            	
           
                exchange.sendResponseHeaders(200, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(405, -1);
            }

            
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            ScheduledFuture<?> result = executor.scheduleAtFixedRate(reset, 60, 60, TimeUnit.SECONDS);
            
            
            exchange.close();
        }));
                
        server.setExecutor(null);
        server.start();

    }
    
    private static void urlGET(String url) {
    	HttpClient client = HttpClient.newHttpClient();
    	HttpRequest request = HttpRequest.newBuilder()
    	      .uri(URI.create(url))
    	      .build();
    	client.sendAsync(request, BodyHandlers.ofString())
    	      .thenApply(HttpResponse::body)
    	      .thenAccept(System.out::println)
    	      .join();
		
	}
    
    public static Map<String, List<String>> splitQuery(String query) {
        if (query == null || "".equals(query)) {
            return Collections.emptyMap();
        }
        
        return Pattern.compile("&").splitAsStream(query)
            .map(s -> Arrays.copyOf(s.split("="), 2))
            .collect(groupingBy(s -> decode(s[0]), mapping(s -> decode(s[1]), toList())));

    }

    private static String decode(final String encoded) {
        try {
            return encoded == null ? null : URLDecoder.decode(encoded, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is a required encoding", e);
        }
    }
}