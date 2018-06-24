package hcmus;

import hcmus.base.ILifeCycleContract;

import java.io.IOException;

public interface IServerContract {
    interface View extends ILifeCycleContract.View {
    }

    interface Controller {
        void startListenConnections();
    }
}
