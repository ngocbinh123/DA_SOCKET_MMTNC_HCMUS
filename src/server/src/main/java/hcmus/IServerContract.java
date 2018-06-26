package hcmus;



public interface IServerContract {
    interface View extends ILifeCycleContract.View {
    }

    interface Controller {
        void startListenConnections();
    }
}
