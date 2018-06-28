package hcmus;

public interface ISocketServerContract {
    interface View extends ILifeCycleContract.View {
    }

    interface Controller {
        void startListenConnections();
    }
}
