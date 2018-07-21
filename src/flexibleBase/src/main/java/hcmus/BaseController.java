package hcmus;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class BaseController<V extends ILifeCycleContract.View> implements ILifeCycleContract.Controller<V> {
    protected static int SERVER_PORT = 6789;
    private WeakReference<V> mWeekRef;
    protected V getView() {
        return mWeekRef.get();
    }

    @Override
    public void detachView() {
        mWeekRef.clear();
        mWeekRef = null;
    }

    @Override
    public void attachView(ILifeCycleContract.View view) {
        mWeekRef = new WeakReference(view);
        view.setController(this);
    }

    protected String getIP() {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "127.0.0.1";
    }
}
