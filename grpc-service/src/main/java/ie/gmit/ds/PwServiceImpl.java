package ie.gmit.ds;

import java.util.ArrayList;
import java.util.logging.Logger;
import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

// Adapted from the grpc-async-inventory lab

public class PwServiceImpl extends InventoryServiceGrpc.InventoryServiceImplBase {
	private ArrayList<Item> itemsList;
	private static final Logger logger = Logger.getLogger(PwServiceImpl.class.getName());

	public PwServiceImpl() {
		itemsList = new ArrayList<>();
		createDummyItems();
	}

	@Override
	public void addItem(Item item, StreamObserver<BoolValue> responseObserver) {
		try {
			itemsList.add(item);
			logger.info("Added new item: " + item);
			responseObserver.onNext(BoolValue.newBuilder().setValue(true).build());
		} catch (RuntimeException ex) {
			responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
		}
		responseObserver.onCompleted();
	}

	@Override
	public void getItems(Empty request, StreamObserver<Items> responseObserver) {
		Items.Builder items = Items.newBuilder();
		for (Item item : itemsList) {
			items.addItems(item);
		}
		responseObserver.onNext(items.build());
		responseObserver.onCompleted();
	}

//Let's also populate the inventory with some dummy items to help us test:
//Code to populate inventory
	private void createDummyItems() {
		itemsList.add(Item.newBuilder().setName("First Item").setId("001").setDescription("A cool item").build());
		itemsList.add(
				Item.newBuilder().setName("Second Item").setId("002").setDescription("An even cooler item").build());
		itemsList.add(Item.newBuilder().setName("Third Item").setId("003").setDescription("A crap item").build());
	}
}