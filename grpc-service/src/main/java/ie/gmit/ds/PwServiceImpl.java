package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import io.grpc.stub.StreamObserver;

// Adapted from the grpc-async-inventory lab

public class PwServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {

	@Override
	public void validate(ValidateRequest request, StreamObserver<BoolValue> response) {


	}

	@Override
	public void hash(HashRequest resquest, StreamObserver<HashResponse> response) {
	}
}