package top.youlanqiang.netty.decorator;

public class Test {
    public static void main(String[] args) {
        Component component = new ConcreteDecorator1(new ConcreteComponent());
        component.doSomething();
    }
}
