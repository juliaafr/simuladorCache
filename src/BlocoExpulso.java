public class BlocoExpulso {

    public final int rotulo;
    public final boolean sujo; //bit D: se true, gera writeback

    public BlocoExpulso(int rotulo, boolean sujo) {
        this.rotulo = rotulo;
        this.sujo = sujo;
    }
}
