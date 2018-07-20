package hcmus;

import java.net.InetAddress;
import java.net.UnknownHostException;

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

    protected String getRemoteIP() {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return null;
    }
}

interface IMainMethod {
    void onCreate();
    void onDestroy();
}