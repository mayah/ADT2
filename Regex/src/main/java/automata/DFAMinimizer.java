package automata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class DFAMinimizer {

    public static DFA minimize(DFA dfa) {
        List<DFANode> nodes = dfa.makeNodeList();
        HashMap<DFANode, Integer> nodeToId = new HashMap<DFANode, Integer>();
        for (int i = 0; i < nodes.size(); ++i) {
            nodeToId.put(nodes.get(i), i);
        }

        boolean[][] different = new boolean[nodes.size()][nodes.size()];

        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < nodes.size(); ++i) {
                for (int j = i + 1; j < nodes.size(); ++j) {
                    if (different[i][j])
                        continue;

                    DFANode lhs = nodes.get(i);
                    DFANode rhs = nodes.get(j);
                    if (lhs.isFinal() != rhs.isFinal()) {
                        changed = true;
                        different[i][j] = true;
                        different[j][i] = true;
                    }

                    Set<Character> keys = new HashSet<Character>();
                    keys.addAll(lhs.getEdges().keySet());
                    keys.addAll(rhs.getEdges().keySet());

                    for (char c : keys) {
                        DFANode n1 = lhs.transit(c);
                        DFANode n2 = rhs.transit(c);

                        assert (n1 != null || n2 != null);

                        if (n1 == null || n2 == null) {
                            different[i][j] = true;
                            different[j][i] = true;
                            changed = true;
                            continue;
                        }

                        int id1 = nodeToId.get(n1);
                        int id2 = nodeToId.get(n2);
                        if (different[id1][id2]) {
                            changed = true;
                            different[i][j] = true;
                            different[j][i] = true;
                        }
                    }
                }
            }
        }

        int[] converted = new int[different.length];
        for (int i = 0; i < different.length; ++i) {
            converted[i] = i;
            for (int j = 0; j < i; ++j) {
                if (!different[i][j])
                    converted[i] = j;
            }
        }

        DFANode[] newNodes = new DFANode[different.length];
        for (int i = 0; i < different.length; ++i)
            newNodes[i] = new DFANode(nodes.get(i).isFinal());

        for (int i = 0; i < nodes.size(); ++i) {
            DFANode n = nodes.get(i);
            for (Entry<Character, DFANode> entry : n.getEdges().entrySet()) {
                newNodes[converted[i]].addEdge(entry.getKey(), newNodes[converted[nodeToId.get(entry.getValue())]]);
            }
        }

        return new DFA(newNodes[converted[nodeToId.get(dfa.getBeginNode())]]);
    }
}
