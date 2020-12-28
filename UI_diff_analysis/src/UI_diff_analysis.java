import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.IRIDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;

public class UI_diff_analysis {
	
	
	
	
	
	//Get signature stats by reading the rdf comments
		public void analyse_diffs(String filePath_1, String filePath_2) throws Exception {
			OWLOntologyManager manager1 = OWLManager.createOWLOntologyManager();
			
			File file1 = new File(filePath_1);
			IRI iri1 = IRI.create(file1);
			OWLOntology witnesses_1 = manager1.loadOntologyFromOntologyDocument(new IRIDocumentSource(iri1),
					new OWLOntologyLoaderConfiguration().setLoadAnnotationAxioms(true));
			
			
			System.out.println("the witnesses_1 axioms size: " + witnesses_1.getLogicalAxiomCount());
			System.out.println("the witnesses_1 classes size: " + witnesses_1.getClassesInSignature().size());
			System.out.println("the witnesses_1 properties size: " + witnesses_1.getObjectPropertiesInSignature().size());
			
			
			OWLOntologyManager manager2 = OWLManager.createOWLOntologyManager();
			
			File file2 = new File(filePath_2);
			IRI iri2 = IRI.create(file2);
			OWLOntology witnesses_2 = manager2.loadOntologyFromOntologyDocument(new IRIDocumentSource(iri2),
					new OWLOntologyLoaderConfiguration().setLoadAnnotationAxioms(true));
			
			
			System.out.println("the witnesses_2 axioms size: " + witnesses_2.getLogicalAxiomCount());
			System.out.println("the witnesses_2 classes size: " + witnesses_2.getClassesInSignature().size());
			System.out.println("the witnesses_2 properties size: " + witnesses_2.getObjectPropertiesInSignature().size());
			
			
			
			Set<OWLEntity> entities_1 = new HashSet<>();
			Set<OWLClass> classes_1 = witnesses_1.getClassesInSignature();
			Set<OWLObjectProperty> properties_1 = witnesses_1.getObjectPropertiesInSignature();
			entities_1.addAll(properties_1);
			entities_1.addAll(classes_1);
			System.out.println("the entities_1 size: " + entities_1.size());
			
			//the size of the total classes in subontology
			Set<OWLClass> total_classes_1 = new HashSet<>();
			
			Set<OWLClass> total_focus_classes_1 = new HashSet<>();
			Set<OWLClass> defined_focus_classes_1 = new HashSet<>();
			Set<OWLClass> primitive_focus_classes_1 = new HashSet<>();
			
			
			Set<OWLClass> total_supporting_classes_1 = new HashSet<>();
			Set<OWLClass> defined_supporting_classes_1 = new HashSet<>();
			Set<OWLClass> primitive_supporting_classes_1 = new HashSet<>();
			
			Set<OWLObjectProperty> total_properties_1 = new HashSet<>();
			
			Set<OWLObjectProperty> total_focus_properties_1 = new HashSet<>();
			
			Set<OWLObjectProperty> total_supporting_properties_1 = new HashSet<>();
		
			
			for(OWLEntity entity : witnesses_1.getSignature()) {
				
				if(entity.isOWLClass()) {
					
					total_classes_1.add(entity.asOWLClass());
					
					Set<OWLAnnotationAssertionAxiom> annotation_assertion_axs = witnesses_1.getAnnotationAssertionAxioms(entity.getIRI());
					for(OWLAnnotationAssertionAxiom ann_ax: annotation_assertion_axs) {
						OWLAnnotation ann = ann_ax.getAnnotation();
						
						System.out.println("the annotation: " + ann);
						
						OWLLiteral literal = (OWLLiteral) ann.getValue();
						String literalString = literal.getLiteral();
						System.out.println("the current literalString: " + literalString);
						if(literalString.contains("Focus class")) {
							total_focus_classes_1.add(entity.asOWLClass());
						}if(literalString.contains("Focus class, defined in the original ontology")) {
							defined_focus_classes_1.add(entity.asOWLClass());
						}
						if(literalString.contains("Focus class, primitive in the original ontology")) {
							primitive_focus_classes_1.add(entity.asOWLClass());
						}
						if(literalString.contains("Supporting class")) {
							total_supporting_classes_1.add(entity.asOWLClass());
						}
						if(literalString.contains("Supporting class, defined in the original ontology")) {
							defined_supporting_classes_1.add(entity.asOWLClass());
						}
						if(literalString.contains("Supporting class, primitive in the original ontology")) {
							primitive_supporting_classes_1.add(entity.asOWLClass());
						}
						
					}
				}else if(entity.isOWLObjectProperty()) {
					total_properties_1.add(entity.asOWLObjectProperty());
					Set<OWLAnnotationAssertionAxiom> annotation_assertion_axs = witnesses_1.getAnnotationAssertionAxioms(entity.getIRI());
					for(OWLAnnotationAssertionAxiom ann_ax: annotation_assertion_axs) {
						OWLAnnotation ann = ann_ax.getAnnotation();
						
						System.out.println("the annotation: " + ann);
						
						OWLLiteral literal = (OWLLiteral) ann.getValue();
						String literalString = literal.getLiteral();
						System.out.println("the current literalString: " + literalString);
						if(literalString.contains("Focus object property")) {
							total_focus_properties_1.add(entity.asOWLObjectProperty());
						}
						
						if(literalString.contains("Supporting object property")) {
							total_supporting_properties_1.add(entity.asOWLObjectProperty());
						}	
					}
				}
			}
			
			System.out.println("the total_classes_1 size: " + total_classes_1.size());
			
			System.out.println("the total_focus_classes_1 size: " + total_focus_classes_1.size());
			System.out.println("the defined_focus_classes_1 size: " + defined_focus_classes_1.size());
			System.out.println("the primitive_focus_classes_1 size: " + primitive_focus_classes_1.size());
			
			System.out.println("the total_supporting_classes_1 size: " + total_supporting_classes_1.size());
			System.out.println("the defined_supporting_classes_1 size: " + defined_supporting_classes_1.size());
			System.out.println("the primitive_supporting_classes_1 size: " + primitive_supporting_classes_1.size());
			
			System.out.println("the total_properties_1 size: " + total_properties_1.size());
			
			System.out.println("the total_focus_properties_1 size: " + total_focus_properties_1.size());
			System.out.println("the total_supporting_properties_1 size: " + total_supporting_properties_1.size());
			

			
			Set<OWLEntity> entities_2 = new HashSet<>();
			Set<OWLClass> classes_2 = witnesses_2.getClassesInSignature();
			Set<OWLObjectProperty> properties_2 = witnesses_2.getObjectPropertiesInSignature();
			entities_2.addAll(properties_2);
			entities_2.addAll(classes_2);
			System.out.println("the entities_2 size: " + entities_2.size());
			
			//the size of the total classes in subontology
			Set<OWLClass> total_classes_2 = new HashSet<>();
			
			Set<OWLClass> total_focus_classes_2 = new HashSet<>();
			Set<OWLClass> defined_focus_classes_2 = new HashSet<>();
			Set<OWLClass> primitive_focus_classes_2 = new HashSet<>();
			
			
			Set<OWLClass> total_supporting_classes_2 = new HashSet<>();
			Set<OWLClass> defined_supporting_classes_2 = new HashSet<>();
			Set<OWLClass> primitive_supporting_classes_2 = new HashSet<>();
			
			Set<OWLObjectProperty> total_properties_2 = new HashSet<>();
			
			Set<OWLObjectProperty> total_focus_properties_2 = new HashSet<>();
			
			Set<OWLObjectProperty> total_supporting_properties_2 = new HashSet<>();
		
			
			for(OWLEntity entity : witnesses_2.getSignature()) {
				
				if(entity.isOWLClass()) {
					
					total_classes_2.add(entity.asOWLClass());
					
					Set<OWLAnnotationAssertionAxiom> annotation_assertion_axs = witnesses_2.getAnnotationAssertionAxioms(entity.getIRI());
					for(OWLAnnotationAssertionAxiom ann_ax: annotation_assertion_axs) {
						OWLAnnotation ann = ann_ax.getAnnotation();
						
						System.out.println("the annotation: " + ann);
						
						OWLLiteral literal = (OWLLiteral) ann.getValue();
						String literalString = literal.getLiteral();
						System.out.println("the current literalString: " + literalString);
						if(literalString.contains("Focus class")) {
							total_focus_classes_2.add(entity.asOWLClass());
						}if(literalString.contains("Focus class, defined in the original ontology")) {
							defined_focus_classes_2.add(entity.asOWLClass());
						}
						if(literalString.contains("Focus class, primitive in the original ontology")) {
							primitive_focus_classes_2.add(entity.asOWLClass());
						}
						if(literalString.contains("Supporting class")) {
							total_supporting_classes_2.add(entity.asOWLClass());
						}
						if(literalString.contains("Supporting class, defined in the original ontology")) {
							defined_supporting_classes_2.add(entity.asOWLClass());
						}
						if(literalString.contains("Supporting class, primitive in the original ontology")) {
							primitive_supporting_classes_2.add(entity.asOWLClass());
						}
						
					}
				}else if(entity.isOWLObjectProperty()) {
					total_properties_2.add(entity.asOWLObjectProperty());
					Set<OWLAnnotationAssertionAxiom> annotation_assertion_axs = witnesses_2.getAnnotationAssertionAxioms(entity.getIRI());
					for(OWLAnnotationAssertionAxiom ann_ax: annotation_assertion_axs) {
						OWLAnnotation ann = ann_ax.getAnnotation();
						
						System.out.println("the annotation: " + ann);
						
						OWLLiteral literal = (OWLLiteral) ann.getValue();
						String literalString = literal.getLiteral();
						System.out.println("the current literalString: " + literalString);
						if(literalString.contains("Focus object property")) {
							total_focus_properties_2.add(entity.asOWLObjectProperty());
						}
						
						if(literalString.contains("Supporting object property")) {
							total_supporting_properties_2.add(entity.asOWLObjectProperty());
						}	
					}
				}
			}
			
			System.out.println("the total_classes_2 size: " + total_classes_2.size());
			
			System.out.println("the total_focus_classes_2 size: " + total_focus_classes_2.size());
			System.out.println("the defined_focus_classes_2 size: " + defined_focus_classes_2.size());
			System.out.println("the primitive_focus_classes_2 size: " + primitive_focus_classes_2.size());
			
			System.out.println("the total_supporting_classes_2 size: " + total_supporting_classes_2.size());
			System.out.println("the defined_supporting_classes_2 size: " + defined_supporting_classes_2.size());
			System.out.println("the primitive_supporting_classes_2 size: " + primitive_supporting_classes_2.size());
			
			System.out.println("the total_properties_2 size: " + total_properties_2.size());
			
			System.out.println("the total_focus_properties_2 size: " + total_focus_properties_2.size());
			System.out.println("the total_supporting_properties_2 size: " + total_supporting_properties_2.size());
			
			
			///Start analysing the differences
			//check if a focus class in the diff set 1 is in the diff set 2
			
			//go through the focus classes in witnesses_1 and check if it exists in witnesses_2 
			//as the following 
				//sub class of
				//equivalent
			//but before that. check if it also exists in witnesses_1 as subclassof or equivalent
				//if this is true. then check if it also as subclass of or equivalent in witnesses_2 
			//<<= do the same check for the opposite direction
			//<<== do the same for supporting concepts
			
			Set<OWLAxiom> cl_focus_1_in_witnesses_2 = new HashSet<>();
			for(OWLClass cl_focus_1: total_focus_classes_1) {
				Set<OWLSubClassOfAxiom> cl_focus_subof_ax_1 = witnesses_1.getSubClassAxiomsForSubClass(cl_focus_1);
				Set<OWLEquivalentClassesAxiom> cl_focus_equiv_ax_1 = witnesses_1.getEquivalentClassesAxioms(cl_focus_1);
				if(cl_focus_subof_ax_1.size() != 0 || cl_focus_equiv_ax_1.size() != 0) {
					Set<OWLSubClassOfAxiom> cl_focus_subof_ax_2 = witnesses_2.getSubClassAxiomsForSubClass(cl_focus_1);
					Set<OWLEquivalentClassesAxiom> cl_focus_equiv_ax_2 = witnesses_2.getEquivalentClassesAxioms(cl_focus_1);
					if(cl_focus_subof_ax_2.size() != 0 || cl_focus_equiv_ax_2.size() != 0) {
						cl_focus_1_in_witnesses_2.addAll(cl_focus_subof_ax_2);
						cl_focus_1_in_witnesses_2.addAll(cl_focus_equiv_ax_2);
					}
				}
			}
			
			Set<OWLAxiom> cl_focus_2_in_witnesses_1 = new HashSet<>();
			for(OWLClass cl_focus_2: total_focus_classes_2) {
				Set<OWLSubClassOfAxiom> cl_focus_subof_ax_2 = witnesses_2.getSubClassAxiomsForSubClass(cl_focus_2);
				Set<OWLEquivalentClassesAxiom> cl_focus_equiv_ax_2 = witnesses_2.getEquivalentClassesAxioms(cl_focus_2);
				if(cl_focus_subof_ax_2.size() != 0 || cl_focus_equiv_ax_2.size() != 0) {
					Set<OWLSubClassOfAxiom> cl_focus_subof_ax_1 = witnesses_1.getSubClassAxiomsForSubClass(cl_focus_2);
					Set<OWLEquivalentClassesAxiom> cl_focus_equiv_ax_1 = witnesses_1.getEquivalentClassesAxioms(cl_focus_2);
					if(cl_focus_subof_ax_1.size() != 0 || cl_focus_equiv_ax_1.size() != 0) {
						cl_focus_2_in_witnesses_1.addAll(cl_focus_subof_ax_1);
						cl_focus_2_in_witnesses_1.addAll(cl_focus_equiv_ax_1);
					}
				}
			}
			
			Set<OWLAxiom> pr_focus_1_in_witnesses_2 = new HashSet<>();
			for(OWLObjectProperty pr_focus_1: total_focus_properties_1) {
				Set<OWLSubObjectPropertyOfAxiom> pr_focus_subof_ax_1 = witnesses_1.getObjectSubPropertyAxiomsForSubProperty(pr_focus_1);
				Set<OWLEquivalentObjectPropertiesAxiom> pr_focus_equiv_ax_1 = witnesses_1.getEquivalentObjectPropertiesAxioms(pr_focus_1);
				if(pr_focus_subof_ax_1.size() != 0 || pr_focus_equiv_ax_1.size() != 0) {
					Set<OWLSubObjectPropertyOfAxiom> pr_focus_subof_ax_2 = witnesses_2.getObjectSubPropertyAxiomsForSubProperty(pr_focus_1);
					Set<OWLEquivalentObjectPropertiesAxiom> pr_focus_equiv_ax_2 = witnesses_2.getEquivalentObjectPropertiesAxioms(pr_focus_1);
					if(pr_focus_subof_ax_2.size() != 0 || pr_focus_equiv_ax_2.size() != 0) {
						pr_focus_1_in_witnesses_2.addAll(pr_focus_subof_ax_2);
						pr_focus_1_in_witnesses_2.addAll(pr_focus_equiv_ax_2);
					}
				}
			}
			
			Set<OWLAxiom> pr_focus_2_in_witnesses_1 = new HashSet<>();
			for(OWLObjectProperty pr_focus_2: total_focus_properties_2) {
				Set<OWLSubObjectPropertyOfAxiom> pr_focus_subof_ax_2 = witnesses_2.getObjectSubPropertyAxiomsForSubProperty(pr_focus_2);
				Set<OWLEquivalentObjectPropertiesAxiom> pr_focus_equiv_ax_2 = witnesses_2.getEquivalentObjectPropertiesAxioms(pr_focus_2);
				if(pr_focus_subof_ax_2.size() != 0 || pr_focus_equiv_ax_2.size() != 0) {
					Set<OWLSubObjectPropertyOfAxiom> pr_focus_subof_ax_1 = witnesses_1.getObjectSubPropertyAxiomsForSubProperty(pr_focus_2);
					Set<OWLEquivalentObjectPropertiesAxiom> pr_focus_equiv_ax_1 = witnesses_1.getEquivalentObjectPropertiesAxioms(pr_focus_2);
					if(pr_focus_subof_ax_1.size() != 0 || pr_focus_equiv_ax_1.size() != 0) {
						pr_focus_2_in_witnesses_1.addAll(pr_focus_subof_ax_1);
						pr_focus_2_in_witnesses_1.addAll(pr_focus_equiv_ax_1);
					}
				}
			}
			
			Set<OWLAxiom> cl_supporting_1_in_witnesses_2 = new HashSet<>();
			for(OWLClass cl_supporting_1: total_supporting_classes_1) {
				Set<OWLSubClassOfAxiom> cl_supporting_subof_ax_1 = witnesses_1.getSubClassAxiomsForSubClass(cl_supporting_1);
				Set<OWLEquivalentClassesAxiom> cl_supporting_equiv_ax_1 = witnesses_1.getEquivalentClassesAxioms(cl_supporting_1);
				if(cl_supporting_subof_ax_1.size() != 0 || cl_supporting_equiv_ax_1.size() != 0) {
					Set<OWLSubClassOfAxiom> cl_supporting_subof_ax_2 = witnesses_2.getSubClassAxiomsForSubClass(cl_supporting_1);
					Set<OWLEquivalentClassesAxiom> cl_supporting_equiv_ax_2 = witnesses_2.getEquivalentClassesAxioms(cl_supporting_1);
					if(cl_supporting_subof_ax_2.size() != 0 || cl_supporting_equiv_ax_2.size() != 0) {
						cl_supporting_1_in_witnesses_2.addAll(cl_supporting_subof_ax_2);
						cl_supporting_1_in_witnesses_2.addAll(cl_supporting_equiv_ax_2);
					}
				}
			}
			
			Set<OWLAxiom> cl_supporting_2_in_witnesses_1 = new HashSet<>();
			for(OWLClass cl_supporting_2: total_supporting_classes_2) {
				Set<OWLSubClassOfAxiom> cl_supporting_subof_ax_2 = witnesses_2.getSubClassAxiomsForSubClass(cl_supporting_2);
				Set<OWLEquivalentClassesAxiom> cl_supporting_equiv_ax_2 = witnesses_2.getEquivalentClassesAxioms(cl_supporting_2);
				if(cl_supporting_subof_ax_2.size() != 0 || cl_supporting_equiv_ax_2.size() != 0) {
					Set<OWLSubClassOfAxiom> cl_supporting_subof_ax_1 = witnesses_1.getSubClassAxiomsForSubClass(cl_supporting_2);
					Set<OWLEquivalentClassesAxiom> cl_supporting_equiv_ax_1 = witnesses_1.getEquivalentClassesAxioms(cl_supporting_2);
					if(cl_supporting_subof_ax_1.size() != 0 || cl_supporting_equiv_ax_1.size() != 0) {
						cl_supporting_2_in_witnesses_1.addAll(cl_supporting_subof_ax_1);
						cl_supporting_2_in_witnesses_1.addAll(cl_supporting_equiv_ax_1);
					}
				}
			}
			
			
			Set<OWLAxiom> pr_supporting_1_in_witnesses_2 = new HashSet<>();
			for(OWLObjectProperty pr_supporting_1: total_supporting_properties_1) {
				Set<OWLSubObjectPropertyOfAxiom> pr_supporting_subof_ax_1 = witnesses_1.getObjectSubPropertyAxiomsForSubProperty(pr_supporting_1);
				Set<OWLEquivalentObjectPropertiesAxiom> pr_supporting_equiv_ax_1 = witnesses_1.getEquivalentObjectPropertiesAxioms(pr_supporting_1);
				if(pr_supporting_subof_ax_1.size() != 0 || pr_supporting_equiv_ax_1.size() != 0) {
					Set<OWLSubObjectPropertyOfAxiom> pr_supporting_subof_ax_2 = witnesses_2.getObjectSubPropertyAxiomsForSubProperty(pr_supporting_1);
					Set<OWLEquivalentObjectPropertiesAxiom> pr_supporting_equiv_ax_2 = witnesses_2.getEquivalentObjectPropertiesAxioms(pr_supporting_1);
					if(pr_supporting_subof_ax_2.size() != 0 || pr_supporting_equiv_ax_2.size() != 0) {
						pr_supporting_1_in_witnesses_2.addAll(pr_supporting_subof_ax_2);
						pr_supporting_1_in_witnesses_2.addAll(pr_supporting_equiv_ax_2);
					}
				}
			}
			
			Set<OWLAxiom> pr_supporting_2_in_witnesses_1 = new HashSet<>();
			for(OWLObjectProperty pr_supporting_2: total_supporting_properties_2) {
				Set<OWLSubObjectPropertyOfAxiom> pr_supporting_subof_ax_2 = witnesses_2.getObjectSubPropertyAxiomsForSubProperty(pr_supporting_2);
				Set<OWLEquivalentObjectPropertiesAxiom> pr_supporting_equiv_ax_2 = witnesses_2.getEquivalentObjectPropertiesAxioms(pr_supporting_2);
				if(pr_supporting_subof_ax_2.size() != 0 || pr_supporting_equiv_ax_2.size() != 0) {
					Set<OWLSubObjectPropertyOfAxiom> pr_supporting_subof_ax_1 = witnesses_1.getObjectSubPropertyAxiomsForSubProperty(pr_supporting_2);
					Set<OWLEquivalentObjectPropertiesAxiom> pr_supporting_equiv_ax_1 = witnesses_1.getEquivalentObjectPropertiesAxioms(pr_supporting_2);
					if(pr_supporting_subof_ax_1.size() != 0 || pr_supporting_equiv_ax_1.size() != 0) {
						pr_supporting_2_in_witnesses_1.addAll(pr_supporting_subof_ax_1);
						pr_supporting_2_in_witnesses_1.addAll(pr_supporting_equiv_ax_1);
					}
				}
			}
			
			System.out.println("the cl_focus_1_in_witnesses_2 size: " + cl_focus_1_in_witnesses_2.size());
			System.out.println("the cl_focus_2_in_witnesses_1 size: " + cl_focus_2_in_witnesses_1.size());
			
			
			System.out.println("the cl_focus_1_in_witnesses_2: " + cl_focus_1_in_witnesses_2);
			System.out.println("the cl_focus_2_in_witnesses_1: " + cl_focus_2_in_witnesses_1);
			
			System.out.println("the cl_supporting_1_in_witnesses_2 size: " + cl_supporting_1_in_witnesses_2.size());
			System.out.println("the cl_supporting_2_in_witnesses_1 size: " + cl_supporting_2_in_witnesses_1.size());
			
			
			System.out.println("the pr_focus_1_in_witnesses_2 size: " + pr_focus_1_in_witnesses_2.size());
			System.out.println("the pr_focus_2_in_witnesses_1 size: " + pr_focus_2_in_witnesses_1.size());
			
			System.out.println("the pr_supporting_1_in_witnesses_2 size: " + pr_supporting_1_in_witnesses_2.size());
			System.out.println("the pr_supporting_2_in_witnesses_1 size: " + pr_supporting_2_in_witnesses_1.size());
			
			
			
		}
	
	
	
	public static void main(String[] args)
			throws Exception {
		
		
		
		
		System.out.println("--- Getting stats of ontologies ---"); 
		//String filePath1 = args[0];
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/Subontologies/era_sct_intl_20200904_new_IRI.owl_snomed_ct_australian.owl_20171231-before-grouping-subontology-v.14.7.owl";		
		String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-sct-intl201907-intl201901/witness_complete_1.owl";
		
		//String filePath1 = "/Users/ghadahalghamdi/Documents/Abstracted_def_based_subontologies/computed-sub-ontologies/updated-subontologies/era_sct_intl_20200904_new_IRI.owl_sct-international_20170731-subontology.owl";
		System.out.println("--------------Ontology 1 file name: " + filePath1 +"--------------"); 
		//String filePath2 = args[1];
		String filePath2 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-sct-intl201901-intl201807/witness_complete_1.owl";
		//String filePath2 = "/Users/ghadahalghamdi/Documents/Abstracted_def_based_subontologies/computed-sub-ontologies/updated-subontologies/era_sct_intl_20200904_new_IRI.owl_snomed_ct_australian.owl_20171231-subontology-v.14.7.owl";
		System.out.println("--------------Ontology 2 file name: " + filePath2 +"--------------"); 
		//String filePath3 = args[2];
		//String filePath3 = "/Users/ghadahalghamdi/Documents/Abstracted_def_based_subontologies/computed-sub-ontologies/updated-subontologies/testing-lethe-ui-diff";
		//System.out.println("--------------Save path is: " + filePath3 +"--------------"); 
		//String add_grouper = args[3]; 
		//System.out.println("--------------add_grouper: " + add_grouper + "--------------");
		//String o_version = args[4];
		//System.out.println("--------------Ontology version: " + o_version +"--------------");

		
		long startTime1 = System.currentTimeMillis(); 
		UI_diff_analysis a = new UI_diff_analysis();
		//diff.using_ui_diff(filePath1, filePath2, filePath3);
		a.analyse_diffs(filePath1, filePath2);
		long endTime1 = System.currentTimeMillis();
		System.out.println("Total Duration = " + (endTime1 - startTime1) + "millis");







	}

}
