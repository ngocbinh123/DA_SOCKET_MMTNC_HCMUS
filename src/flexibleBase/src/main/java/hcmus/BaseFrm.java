package hcmus;

public class BaseFrm implements IMainMethod, ILifeCycleContract.View {
    private BaseController<?> mController;
    @Override
    public void setController(BaseController controller) {
        this.mController = controller;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        mController.detachView();
    }
}

interface IMainMethod {
    void onCreate();
    void onDestroy();
}