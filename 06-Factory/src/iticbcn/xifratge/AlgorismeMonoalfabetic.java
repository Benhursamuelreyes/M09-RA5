package iticbcn.xifratge;

public class AlgorismeMonoalfabetic extends AlgorismeFactory {
    @Override
    public Xifrador creaXifrador() { return new XifradorMonoalfabetic(); }
}