public class HelloWorld {
    public static void main(String[] args) {
        HelloWorld app = new HelloWorld();
        int res = app.add();
        System.out.println(res);
    }

    public int add() {
        int a = 5;
        int b = 7;
        int c = (a + b) * 300;
        return c;
    }
}
