package hcmus;
public interface IClientContract {
    interface View extends ILifeCycleContract.View {
    }

    interface Controller {
        void requestConnectToServer();
    }
}
