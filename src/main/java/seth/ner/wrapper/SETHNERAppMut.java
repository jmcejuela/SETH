package seth.ner.wrapper;

import de.hu.berlin.wbi.objects.MutationMention;
import seth.SETH;
import edu.uchsc.ccp.nlp.ei.mutation.MutationFinder;
import java.util.List;

/**
 * Minimal example to apply {@link SETHNER} (including MutationFinder) on free text
 * @author rockt
 * Date: 11/9/12
 * Time: 11:12 AM
 */

public class SETHNERAppMut {

    public static void main(String[] args) {
        String text = args[0];
        Boolean useMutationFinderOnly = (args.length > 1) ? Boolean.parseBoolean(args[1]) : false;
        MutationFinder mf = new MutationFinder(); //With the original regex I got the error: Input residue not recognized, must be a standard residue: ' ' --> // (useMutationFinderOnly) ? MutationFinder.useOriginalRegex() : new MutationFinder();

        SETH seth = new SETH(mf, true, true);

        List<MutationMention> result = (useMutationFinderOnly) ? seth.findMutationsWithMutationFinder(text) : seth.findMutations(text);
        for (MutationMention mutation : result) {
            System.out.println(mutation.toBratFormat());
        }
    }
}
