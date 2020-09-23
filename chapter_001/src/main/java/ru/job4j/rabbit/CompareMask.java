package ru.job4j.rabbit;

class CompareMask {
    private int nS;
    private int nP;
    private int lenStr;
    private int lenPat;
    private String[] str;
    private String[] pat;
    private boolean res = true;

    CompareMask(final String str, final String pat) {
        this.str = str.split("\\.");
        this.pat = pat.split("\\.");
    }

    boolean compare() {
        lenStr = str.length;
        lenPat = pat.length;
        String s;
        String p;
        do {
            if (nP == lenPat) {
                res = false;
                break;
            }
            p = pat[nP];
            s = str[nS];
            if (p.equals("#")) {
                if (manyWord()) {
                    break;
                }
            } else if (s.equals(p) || p.equals("*")) {
                nS++;
                nP++;
            } else {
                res = false;
                break;
            }
        } while (nS < lenStr);
        if (lenPat > lenStr) {
            res = false;
        }
        return res;
    }

    boolean manyWord() {
        nS++;
        if (++nP == lenPat) {
            return true;
        }
        while (nS < lenStr) {
            if (!str[nS].equals(pat[nP])) {
                nS++;
            } else {
                return false;
            }
        }
        nS--;
        return false;
    }
}
