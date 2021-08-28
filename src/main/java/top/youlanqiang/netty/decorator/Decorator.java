package top.youlanqiang.netty.decorator;

public class Decorator implements Component{

    private final Component component;

    public Decorator(Component component) {
        this.component = component;
    }

    @Override
    public void doSomething() {
        component.doSomething();
    }
}
