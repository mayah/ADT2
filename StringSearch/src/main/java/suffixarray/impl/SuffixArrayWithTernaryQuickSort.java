package suffixarray.impl;

import suffixarray.SuffixArray;
import suffixarray.TernaryQuickSort;
import suffixarray.SuffixArray.SuffixArrayRange;

public class SuffixArrayWithTernaryQuickSort extends SuffixArray {
    public SuffixArrayWithTernaryQuickSort(String text) {
        super(text);
    }

    @Override
    protected void sort() {
        TernaryQuickSort.sort(data);
    }

    @Override
    public SuffixArrayRange find(String s) {
        return new SuffixArrayRange(findLowerBound(s), findUpperBound(s));
    }

    private int findLowerBound(String s) {
        int left = -1;
        int right = data.length - 1;

        while (left + 1 < right) {
            int mid = (left + right) / 2;
            String midSubstring = data[mid].suffix.substring(0, s.length());
            if (midSubstring.compareTo(s) < 0)
                left = mid;
            else
                right = mid;
        }

        return right;
    }

    private int findUpperBound(String s) {
        int left = 0;
        int right = data.length;

        while (left + 1 < right) {
            int mid = (left + right) / 2;
            String midSubstring = data[mid].suffix.substring(0, s.length());
            if (midSubstring.compareTo(s) <= 0)
                left = mid;
            else
                right = mid;
        }

        return right;
    }
}
