package seth.ner.wrapper;

import de.hu.berlin.wbi.objects.MutationMention;
import seth.SETH;

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
        String mutregexfile = (args.length > 2) ? args[1] : "resources/mutations.txt";
        
        SETH seth = new SETH(mutregexfile, true, true);

        List<MutationMention> result = seth.findMutations(text);
        for (MutationMention mutation : result) {
            System.out.println(mutation.toBratFormat());
        }
    }
}
