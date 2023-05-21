//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package chordvers.lanstard.tools;

public class RandomInt {
    public RandomInt() {
    }

    public Integer dtoi(Double Num) {
        String s1 = Num.toString();
        String[] s2 = s1.split("\\.");
        Integer i = Integer.parseInt(s2[0]);
        return i;
    }

    public Integer RandomInt(Integer Min, Integer Max) {
        Integer num = Max + 1 - Min;
        Integer i = this.dtoi(Math.random() * (double)num + (double)Min);
        return i;
    }
}
