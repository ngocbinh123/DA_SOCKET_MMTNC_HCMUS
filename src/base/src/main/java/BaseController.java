import java.lang.ref.WeakReference;

public class BaseController<V extends ILifeCycleContract.View> implements ILifeCycleContract.Controller<V> {
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
}
