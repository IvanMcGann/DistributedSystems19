package ie.gmit.ds;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class ClientServer {

	// logger used to log message for application component
	private static final Logger logger = Logger.getLogger(ClientServer.class.getName());
	private final ManagedChannel channel;
	private final PasswordServiceGrpc.PasswordServiceStub asyncService;
	private final PasswordServiceGrpc.PasswordServiceBlockingStub syncService;
	private ByteString expectedHash;
	private ByteString salt;

	public ClientServer(String host, int port) {
		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		syncService = PasswordServiceGrpc.newBlockingStub(channel);
		asyncService = PasswordServiceGrpc.newStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public static void main(String[] args) throws Exception {
		ClientServer client = new ClientServer("localhost", 50551);

	}

	// getters
	public ByteString getExpectedHash() {
		return expectedHash;
	}

	public ByteString getSalt() {
		return salt;
	}

	public void hash(int userId, String password) {

		StreamObserver<HashResponse> hr = new StreamObserver<HashResponse>() {

			@Override
			public void onNext(HashResponse value) {
				// TODO Auto-generated method stub
				salt = value.getSalt();
				expectedHash = value.getHashPassword();

			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub

			}
		};

		try {
			logger.info("Retrieve users");
			HashRequest hrq = HashRequest.newBuilder().setUserId(userId).setPassword(password).build();
			asyncService.hash(hrq, hr);
			TimeUnit.SECONDS.sleep(10);
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
		return;
	}

	public boolean validate(ByteString hash, ByteString salt, String password) {

		try {
			BoolValue value = syncService.validate(
					ValidateRequest.newBuilder().setHashPassword(hash).setSalt(salt).setPassword(password).build());
			return value.getValue();
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

}