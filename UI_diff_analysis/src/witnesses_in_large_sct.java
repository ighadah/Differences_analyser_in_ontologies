import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.io.IRIDocumentSource;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import com.google.common.collect.Sets;

public class witnesses_in_large_sct {
	
	
	public void check_sig_witnesses(String filePath_wit, String ref_set_sig_file) throws OWLOntologyCreationException, FileNotFoundException, OWLOntologyStorageException {
		
		OWLOntologyManager manager1 = OWLManager.createOWLOntologyManager();
		
		File file1 = new File(filePath_wit);
		IRI iri1 = IRI.create(file1);
		OWLOntology witnesses = manager1.loadOntologyFromOntologyDocument(new IRIDocumentSource(iri1),
				new OWLOntologyLoaderConfiguration().setLoadAnnotationAxioms(true));
		
		
		System.out.println("the witnesses axioms size: " + witnesses.getLogicalAxiomCount());
		System.out.println("the witnesses classes size: " + witnesses.getClassesInSignature().size());
		System.out.println("the witnesses properties size: " + witnesses.getObjectPropertiesInSignature().size());
		
		OWLOntologyManager manager2 = OWLManager.createOWLOntologyManager();
		
		File file2 = new File(ref_set_sig_file);
		IRI iri2 = IRI.create(file2);
		OWLOntology ref_set_sig = manager2.loadOntologyFromOntologyDocument(new IRIDocumentSource(iri2),
				new OWLOntologyLoaderConfiguration().setLoadAnnotationAxioms(true));
		
		
		System.out.println("the ref_set_sig axioms size: " + ref_set_sig.getLogicalAxiomCount());
		System.out.println("the ref_set_sig classes size: " + ref_set_sig.getClassesInSignature().size());
		System.out.println("the ref_set_sig properties size: " + ref_set_sig.getObjectPropertiesInSignature().size());
		
		OWLOntologyManager manager3 = OWLManager.createOWLOntologyManager();
		
		OWLOntology witnesses_for_analysis = manager3.createOntology();
		//remove the logical axioms from the witnesses_for_analysis
		//Set<OWLLogicalAxiom> axioms_to_remove = witnesses_for_analysis.getLogicalAxioms();
		//manager3.removeAxioms(witnesses_for_analysis, axioms_to_remove);
		//the ref set signature
		Set<OWLEntity> ref_set_sig_entities = new HashSet<>(ref_set_sig.getSignature());
		
		for(OWLLogicalAxiom axiom: witnesses.getLogicalAxioms()) {
			
			Set<OWLEntity> axiom_sig = new HashSet<>(axiom.getSignature());
			Set<OWLEntity> axiom_sig_intersect_ref_set = new HashSet<>(Sets.intersection(ref_set_sig_entities, axiom_sig));
			if(!axiom_sig_intersect_ref_set.isEmpty()) {
				manager3.addAxiom(witnesses_for_analysis, axiom);
			}
		}
		
		System.out.println("the witnesses_for_analysis axioms size: " + witnesses_for_analysis.getLogicalAxiomCount());
		System.out.println("the witnesses_for_analysis classes size: " + witnesses_for_analysis.getClassesInSignature().size());
		System.out.println("the witnesses_for_analysis properties size: " + witnesses_for_analysis.getObjectPropertiesInSignature().size());
		OWLDataFactory df = manager3.getOWLDataFactory();
		//go through witnesses to add the annotations
		for(OWLEntity en: witnesses_for_analysis.getSignature()) {
			if(ref_set_sig.getSignature().contains(en)) {
				
				
				OWLAnnotation commentAnnotation = df.getOWLAnnotation(df.getRDFSComment(),df.getOWLLiteral("GD entity"));
				OWLAxiom ax = df.getOWLAnnotationAssertionAxiom(en.getIRI(), commentAnnotation);
				manager3.addAxiom(witnesses_for_analysis, ax);
				
			}
			Set<OWLAnnotationAssertionAxiom> annotations = witnesses.getAnnotationAssertionAxioms(en.getIRI());
			manager3.addAxioms(witnesses_for_analysis, annotations);
		}
		OutputStream ops_before = new FileOutputStream(new File(ref_set_sig_file + "_witness_implicit_2_202003_witnesses_for_analysis.owl"));
	
		manager3.saveOntology(witnesses_for_analysis, new FunctionalSyntaxDocumentFormat(), ops_before);
	
	}
	
	public void get_lhs_GD_ref_set_from_witnesses(String owlPath) throws OWLOntologyCreationException, FileNotFoundException, OWLOntologyStorageException {
		
		
		OWLOntologyManager manager1 = OWLManager.createOWLOntologyManager();
		
		File file1 = new File(owlPath);
		IRI iri1 = IRI.create(file1);
		OWLOntology witnesses = manager1.loadOntologyFromOntologyDocument(new IRIDocumentSource(iri1),
				new OWLOntologyLoaderConfiguration().setLoadAnnotationAxioms(true));
		
		
		System.out.println("the witnesses axioms size: " + witnesses.getLogicalAxiomCount());
		System.out.println("the witnesses classes size: " + witnesses.getClassesInSignature().size());
		System.out.println("the witnesses properties size: " + witnesses.getObjectPropertiesInSignature().size());
		
		
		OWLOntologyManager manager2 = OWLManager.createOWLOntologyManager();
		Set<OWLAxiom> axioms_total = new HashSet<>();
		Set<OWLAxiom> axioms_with_lhs = new HashSet<>();
		Set<OWLAxiom> axioms_with_rhs = new HashSet<>();
		Set<OWLAxiom> axioms_with_equivs = new HashSet<>();
		for(OWLLogicalAxiom ax_to_analyse: witnesses.getLogicalAxioms()) {
			//the axiom with GD entity in lhs
			if(ax_to_analyse.isOfType(AxiomType.SUBCLASS_OF)) {
				OWLSubClassOfAxiom ax_to_analyse_subof = (OWLSubClassOfAxiom) ax_to_analyse;
				if(!ax_to_analyse_subof.isGCI()) {
					OWLClassExpression lhs = ax_to_analyse_subof.getSubClass();
					//OWLClassExpression rhs = ax_to_analyse_subof.getSuperClass();
					//get sig of lhs
					for(OWLEntity en: lhs.getSignature()) {
						Set<OWLAnnotationAssertionAxiom> annotation_assertion_axs = witnesses.getAnnotationAssertionAxioms(en.getIRI());
							for(OWLAnnotationAssertionAxiom ann_ax: annotation_assertion_axs) {
								OWLAnnotation ann = ann_ax.getAnnotation();
								OWLLiteral literal = (OWLLiteral) ann.getValue();
								String literalString = literal.getLiteral();
								if(literalString.contains("Focus class")) {
									//axioms_with_lhs_or_rhs_gd.add(ax_to_analyse);
									axioms_with_lhs.add(ax_to_analyse);
									axioms_total.add(ax_to_analyse);
								}
							}
						
					}
					
				}else if(ax_to_analyse_subof.isGCI()) {
					
					OWLClassExpression rhs = ax_to_analyse_subof.getSuperClass();
					for(OWLEntity en: rhs.getSignature()) {
						Set<OWLAnnotationAssertionAxiom> annotation_assertion_axs = witnesses.getAnnotationAssertionAxioms(en.getIRI());
							for(OWLAnnotationAssertionAxiom ann_ax: annotation_assertion_axs) {
								OWLAnnotation ann = ann_ax.getAnnotation();
								OWLLiteral literal = (OWLLiteral) ann.getValue();
								String literalString = literal.getLiteral();
								if(literalString.contains("Focus class")) {
									//axioms_with_lhs_or_rhs_gd.add(ax_to_analyse);
									axioms_with_rhs.add(ax_to_analyse);
									axioms_total.add(ax_to_analyse);
								}
							}
						
					}
				}
				
			}else if(ax_to_analyse.isOfType(AxiomType.EQUIVALENT_CLASSES)) {
				OWLEquivalentClassesAxiom ax_to_analyse_equiv = (OWLEquivalentClassesAxiom) ax_to_analyse;
				Set<OWLSubClassOfAxiom> ax_to_analyse_equiv_subofs = ax_to_analyse_equiv.asOWLSubClassOfAxioms();
				for(OWLSubClassOfAxiom ax_to_analyse_equiv_subof: ax_to_analyse_equiv_subofs) {
					if(!ax_to_analyse_equiv_subof.isGCI()) {
						OWLClassExpression lhs = ax_to_analyse_equiv_subof.getSubClass();
						OWLClassExpression rhs = ax_to_analyse_equiv_subof.getSuperClass();
						//get sig of lhs
						for(OWLEntity en: lhs.getSignature()) {
							Set<OWLAnnotationAssertionAxiom> annotation_assertion_axs = witnesses.getAnnotationAssertionAxioms(en.getIRI());
								for(OWLAnnotationAssertionAxiom ann_ax: annotation_assertion_axs) {
									OWLAnnotation ann = ann_ax.getAnnotation();
									OWLLiteral literal = (OWLLiteral) ann.getValue();
									String literalString = literal.getLiteral();
									if(literalString.contains("Focus class")) {
										axioms_with_equivs.add(ax_to_analyse);
										axioms_total.add(ax_to_analyse);
									}
								}
							
						}
					}
				}
				
			}
		}
		
		System.out.println("the UPWARD witnesses size: " + axioms_with_lhs.size());
		System.out.println("the DOWNWARD witnesses size: " + axioms_with_rhs.size());
		System.out.println("the EQUIVS size: " + axioms_with_equivs.size());
		OWLOntology O_to_save = manager2.createOntology();
		manager2.addAxioms(O_to_save, axioms_total);
		
		//OWLDataFactory df = manager2.getOWLDataFactory();
		
		for(OWLEntity en: O_to_save.getSignature()) {
			
			Set<OWLAnnotationAssertionAxiom> annotations = witnesses.getAnnotationAssertionAxioms(en.getIRI());
			manager2.addAxioms(O_to_save, annotations);
		}
		
		
		
		OutputStream ops_before = new FileOutputStream(new File(owlPath + "_all_GD.owl"));
		
		System.out.println("the O_to_save axioms size: " + O_to_save.getLogicalAxiomCount());
		System.out.println("the O_to_save classes size: " + O_to_save.getClassesInSignature().size());
		System.out.println("the O_to_save properties size: " + O_to_save.getObjectPropertiesInSignature().size());
		manager2.saveOntology(O_to_save, new FunctionalSyntaxDocumentFormat(), ops_before);
	}
	
	
	public static void main(String[] args)
			throws Exception {
		
		
		
		
		System.out.println("--- Extracting Ref set witnessess ---"); 
		//String filePath1 = args[0];
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/Subontologies/era_sct_intl_20200904_new_IRI.owl_snomed_ct_australian.owl_20171231-before-grouping-subontology-v.14.7.owl";		
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-sct-intl201907-intl201901/witness_complete_1.owl";
		
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/sct-intl201707-intl201701/witness_implicit_2.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/sct-intl201801-intl201707/witness_implicit_2.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-star-mod-intl201707-intl201701/witness_implicit_1.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-star-mod-intl201801-intl201707/witness_implicit_2.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-star-mod-intl201807-intl201801/witness_implicit_2.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-star-mod-intl201901-intl201807/witness_implicit_2.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-star-mod-intl201907-intl201901/witness_implicit_2.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-star-mod-intl202001-intl201907/witness_implicit_1.owl";
		
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-star-mod-intl201707-intl201701/sct-GD-refset-names.txt_sig_ontology.owl_witness_complete_1_201707_witnesses_for_analysis.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-comparisons-module-common-sig/ERA-sct-intl201707-intl201701/ERA-sct-intl201707-intl201701/witness_complete_2.owl";
		
		
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-sct-intl202007-intl202003/witness_implicit_2.owl";
		
		
		
	//	String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-star-mod-intl202007-intl202003/sct-GD-refset-names.txt_sig_ontology.owl_witness_implicit_2_202003_witnesses_for_analysis.owl";
		
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/test-LBMS.owl/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-star-mod-intl201707-intl201701/sct-GD-refset-names.txt_sig_ontology.owl_witness_complete_1_201707_witnesses_for_analysis.owl";
		
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-star-mod-intl201707-intl201701/sct-GD-refset-names.txt_sig_ontology.owl_witness_complete_1_201707_witnesses_for_analysis.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-star-mod-intl201707-intl201701/sct-GD-refset-names.txt_sig_ontology.owl_witness_explicit_1_witnesses_for_analysis.owl";
		
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-star-mod-intl201707-intl201701/sct-GD-refset-names.txt_sig_ontology.owl_witness_implicit_1_witnesses_for_analysis.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-star-mod-intl201707-intl201701/sct-GD-refset-names.txt_sig_ontology.owl_witness_complete_2_201701_witnesses_for_analysis.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-star-mod-intl201707-intl201701/sct-GD-refset-names.txt_sig_ontology.owl_witness_explicit_2_witnesses_for_analysis.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/large-sct-ontologies-comparisons/GD-star-mod-comparisons/GD-star-mod-intl202007-intl202003/sct-GD-refset-names.txt_sig_ontology.owl_witness_implicit_2_202003_witnesses_for_analysis.owl";
		//MRI
		String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/GD-2/GD-subont-comparisons-201701-201707/witness_complete_in_201701.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/MRI/MRI-sct-intl201801-intl201707/MRI-sct-intl201801-intl201707/witness_complete_2.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/Abstracted_def_based_subontologies/computed-sub-ontologies/updated-subontologies/era_sct_intl_20200904_new_IRI.owl_sct-international_20170731-subontology.owl";
		System.out.println("--------------Witnesses Ontology file name: " + filePath1 +"--------------"); 
		//String filePath2 = args[1];
		//String filePath2 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-sct-intl201901-intl201807/witness_complete_1.owl";
		//String filePath2 = "/Users/ghadahalghamdi/Documents/Abstracted_def_based_subontologies/computed-sub-ontologies/updated-subontologies/era_sct_intl_20200904_new_IRI.owl_snomed_ct_australian.owl_20171231-subontology-v.14.7.owl";
		String filePath2 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-GD-refset-names.txt_sig_ontology.owl";
		//MRI
		//String filePath2 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/MRI/MRI-sct-intl201801-intl201707/MRI-sct-intl201801-intl201707/witness_complete_1.owl";
		//String filePath2 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-comparisons-module-common-sig/ERA-sct-intl201707-intl201701/ERA-sct-intl201707-intl201701/witness_complete_1.owl";
		System.out.println("--------------Ref set Sig file name: " + filePath2 +"--------------"); 
		
		
		//String filePath3 = args[2];
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/Subontologies/era_sct_intl_20200904_new_IRI.owl_snomed_ct_australian.owl_20171231-before-grouping-subontology-v.14.7.owl";		
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-sct-intl201907-intl201901/witness_complete_1.owl";
		
		
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-comparisons-module-common-sig/ERA-sct-intl201707-intl201701/ERA-sct-intl201707-intl201701/witness_complete_2.owl";
		
		//MRI
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/MRI/MRI-sct-intl201801-intl201707/MRI-sct-intl201801-intl201707/witness_complete_2.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/Abstracted_def_based_subontologies/computed-sub-ontologies/updated-subontologies/era_sct_intl_20200904_new_IRI.owl_sct-international_20170731-subontology.owl";
		//System.out.println("--------------SubOntology 1 file name: " + filePath3 +"--------------"); 
		//String filePath4 = args[3];
		///String filePath2 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-sct-intl201901-intl201807/witness_complete_1.owl";
		//String filePath2 = "/Users/ghadahalghamdi/Documents/Abstracted_def_based_subontologies/computed-sub-ontologies/updated-subontologies/era_sct_intl_20200904_new_IRI.owl_snomed_ct_australian.owl_20171231-subontology-v.14.7.owl";
		
		//MRI
		//String filePath2 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/MRI/MRI-sct-intl201801-intl201707/MRI-sct-intl201801-intl201707/witness_complete_1.owl";
		//String filePath2 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-comparisons-module-common-sig/ERA-sct-intl201707-intl201701/ERA-sct-intl201707-intl201701/witness_complete_1.owl";
		//System.out.println("--------------SubOntology 2 file name: " + filePath4 +"--------------"); 
		
		
		
		//String o_1_version = args[4];
		//String o_1_version = "sct-intl201701";
		//String o_1_version = "sct-intl201707";
		//String o_2_version = "sct-intl201801";
		//String filePath3 = args[2];
		//String o_2_version = args[5];
		//String filePath3 = "/Users/ghadahalghamdi/Documents/Abstracted_def_based_subontologies/computed-sub-ontologies/updated-subontologies/testing-lethe-ui-diff";
		//System.out.println("--------------Save path is: " + filePath3 +"--------------"); 
		//String add_grouper = args[3]; 
		//System.out.println("--------------add_grouper: " + add_grouper + "--------------");
		//String o_version = args[4];
		//System.out.println("--------------Ontology version: " + o_version +"--------------");

		
		long startTime1 = System.currentTimeMillis(); 
		witnesses_in_large_sct a = new witnesses_in_large_sct();
		//diff.using_ui_diff(filePath1, filePath2, filePath3);
		//a.analyse_diffs(filePath1, filePath2, filePath3, filePath4, o_1_version, o_2_version);
		//a.check_sig_witnesses(filePath1, filePath2);
		a.get_lhs_GD_ref_set_from_witnesses(filePath1);
		
		long endTime1 = System.currentTimeMillis();
		System.out.println("Total Duration = " + (endTime1 - startTime1) + "millis");







	}
	
	

}
