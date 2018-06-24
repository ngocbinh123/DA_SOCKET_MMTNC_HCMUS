package hcmus;

import hcmus.base.ILifeCycleContract;

public interface IClientContract {
    interface View extends ILifeCycleContract.View {
    }

    interface Controller {
        void requestConnectToServer();
    }
}
