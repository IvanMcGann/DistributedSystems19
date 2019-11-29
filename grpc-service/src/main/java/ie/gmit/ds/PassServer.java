package ie.gmit.ds;

import java.io.IOException;
import java.util.logging.Logger;
import io.grpc.Server;
import io.grpc.ServerBuilder;

// Adapted from the grpc-async-inventory lab

public class PassServer {

	public static void main(String[] args) throws IOException, InterruptedException {
		final PassServer passServer = new PassServer();
		passServer.start();
		passServer.blockUntilShutdown();
	}

	private Server grpcServer;
	private static final Logger logger = Logger.getLogger(PassServer.class.getName());
	private static final int PORT = 50551;
	private String connect = "The Server has started...." + PORT;

	private void start() throws IOException {
		grpcServer = ServerBuilder.forPort(PORT).addService(new PwServiceImpl()).build().start();
		logger.info("Server started, listening on " + PORT);

	}

	private void stop() {
		if (grpcServer != null) {
			grpcServer.shutdown();
		}
	}

	private void blockUntilShutdown() throws InterruptedException {
		if (grpcServer != null) {
			grpcServer.awaitTermination();
		}
	}

}
