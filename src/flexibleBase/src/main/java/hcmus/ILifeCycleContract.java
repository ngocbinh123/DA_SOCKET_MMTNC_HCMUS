package hcmus;

public interface ILifeCycleContract {
    interface View {
        void setController(BaseController controller);
    }

    interface Controller<V extends View> {
        void attachView(V view);
        void detachView();
    }
}
