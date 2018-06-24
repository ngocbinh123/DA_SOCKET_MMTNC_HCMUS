package hcmus.base;

public class BaseFrm implements IMainMethod, ILifeCycleContract.View {
    private BaseController<?> mController;
    public BaseFrm() {
        onCreate();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        mController.detachView();
    }

    @Override
    public void setController(BaseController controller) {
        this.mController = controller;
    }
}

interface IMainMethod {
    void onCreate();
    void onDestroy();
}
