import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.IRIDocumentSource;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

public class UI_diff_analysis_2 {
	
	
	
	
	
	//Get signature stats by reading the rdf comments
		public void analyse_diffs(String filePath_1, String filePath_2, String filePath_3, String filePath_4,String file_1_version, String file_2_version) throws Exception {
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
			
			
			OWLOntologyManager manager3 = OWLManager.createOWLOntologyManager();
			
			File file3 = new File(filePath_3);
			IRI iri3 = IRI.create(file3);
			OWLOntology Subontology_1 = manager3.loadOntologyFromOntologyDocument(new IRIDocumentSource(iri3),
					new OWLOntologyLoaderConfiguration().setLoadAnnotationAxioms(true));
			
			
			System.out.println("the Subontology_1 axioms size: " + Subontology_1.getLogicalAxiomCount());
			System.out.println("the Subontology_1 classes size: " + Subontology_1.getClassesInSignature().size());
			System.out.println("the Subontology_1 properties size: " + Subontology_1.getObjectPropertiesInSignature().size());
			
			
			OWLOntologyManager manager4 = OWLManager.createOWLOntologyManager();
			
			File file4 = new File(filePath_4);
			IRI iri4 = IRI.create(file4);
			OWLOntology Subontology_2 = manager4.loadOntologyFromOntologyDocument(new IRIDocumentSource(iri4),
					new OWLOntologyLoaderConfiguration().setLoadAnnotationAxioms(true));
			
			
			System.out.println("the Subontology_2 axioms size: " + Subontology_2.getLogicalAxiomCount());
			System.out.println("the Subontology_2 classes size: " + Subontology_2.getClassesInSignature().size());
			System.out.println("the Subontology_2 properties size: " + Subontology_2.getObjectPropertiesInSignature().size());
			
			
			
			
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
						
						//System.out.println("the annotation: " + ann);
						
						OWLLiteral literal = (OWLLiteral) ann.getValue();
						String literalString = literal.getLiteral();
						//System.out.println("the current literalString: " + literalString);
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
						
						//System.out.println("the annotation: " + ann);
						
						OWLLiteral literal = (OWLLiteral) ann.getValue();
						String literalString = literal.getLiteral();
						//System.out.println("the current literalString: " + literalString);
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
						
						//System.out.println("the annotation: " + ann);
						if(ann.getValue().isLiteral()) {
						//OWLLiteral literal = (OWLLiteral) ann.getValue();
							//OWLLiteral literal = ann.getValue().asLiteral();
						String literalString = ann.getValue().asLiteral().toString();
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
				}
				}else if(entity.isOWLObjectProperty()) {
					total_properties_2.add(entity.asOWLObjectProperty());
					Set<OWLAnnotationAssertionAxiom> annotation_assertion_axs = witnesses_2.getAnnotationAssertionAxioms(entity.getIRI());
					for(OWLAnnotationAssertionAxiom ann_ax: annotation_assertion_axs) {
						OWLAnnotation ann = ann_ax.getAnnotation();
						
						//System.out.println("the annotation: " + ann);
						
						OWLLiteral literal = (OWLLiteral) ann.getValue();
						String literalString = literal.getLiteral();
						//System.out.println("the current literalString: " + literalString);
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
			
			//create a map that contains the rhs expression of the same cl focus concept from 
			Multimap<OWLClass, OWLAxiom> cl_focus_new_axioms_2 = ArrayListMultimap.create();
			for(OWLAxiom cl_focus_1_in_witnesses_2_ax: cl_focus_1_in_witnesses_2) {
				if(cl_focus_1_in_witnesses_2_ax.isOfType(AxiomType.EQUIVALENT_CLASSES)) {
					OWLEquivalentClassesAxiom equiv = (OWLEquivalentClassesAxiom) cl_focus_1_in_witnesses_2_ax;
					Set<OWLSubClassOfAxiom> subofs = equiv.asOWLSubClassOfAxioms();
					for(OWLSubClassOfAxiom subof: subofs) {
						if(!subof.isGCI()) {
							OWLClassExpression lhs = subof.getSubClass();
							cl_focus_new_axioms_2.put(lhs.asOWLClass(), subof);
						}
					}
				}else if(cl_focus_1_in_witnesses_2_ax.isOfType(AxiomType.SUBCLASS_OF)) {
					OWLSubClassOfAxiom subof = (OWLSubClassOfAxiom) cl_focus_1_in_witnesses_2_ax;
					if(!subof.isGCI()) {
						OWLClassExpression lhs = subof.getSubClass();
						cl_focus_new_axioms_2.put(lhs.asOWLClass(), subof);
					}
				}
			}
			Multimap<OWLClass, OWLAxiom> cl_focus_old_axioms_1 = ArrayListMultimap.create();
			for(OWLAxiom cl_focus_2_in_witnesses_1_ax: cl_focus_2_in_witnesses_1) {
				if(cl_focus_2_in_witnesses_1_ax.isOfType(AxiomType.EQUIVALENT_CLASSES)) {
					OWLEquivalentClassesAxiom equiv = (OWLEquivalentClassesAxiom) cl_focus_2_in_witnesses_1_ax;
					Set<OWLSubClassOfAxiom> subofs = equiv.asOWLSubClassOfAxioms();
					for(OWLSubClassOfAxiom subof: subofs) {
						if(!subof.isGCI()) {
							OWLClassExpression lhs = subof.getSubClass();
							cl_focus_old_axioms_1.put(lhs.asOWLClass(), equiv);
						}
					}
				}else if(cl_focus_2_in_witnesses_1_ax.isOfType(AxiomType.SUBCLASS_OF)) {
					OWLSubClassOfAxiom subof = (OWLSubClassOfAxiom) cl_focus_2_in_witnesses_1_ax;
					if(!subof.isGCI()) {
						OWLClassExpression lhs = subof.getSubClass();
						cl_focus_old_axioms_1.put(lhs.asOWLClass(), subof);
					}
				}
			}
			
			
			
			
			//create a map that contains the rhs expression of the same cl focus concept from 
			Multimap<OWLClass, OWLAxiom> cl_supporting_new_axioms_2 = ArrayListMultimap.create();
			for(OWLAxiom cl_supporting_1_in_witnesses_2_ax: cl_supporting_1_in_witnesses_2) {
				if(cl_supporting_1_in_witnesses_2_ax.isOfType(AxiomType.EQUIVALENT_CLASSES)) {
					OWLEquivalentClassesAxiom equiv = (OWLEquivalentClassesAxiom) cl_supporting_1_in_witnesses_2_ax;
					Set<OWLSubClassOfAxiom> subofs = equiv.asOWLSubClassOfAxioms();
					for(OWLSubClassOfAxiom subof: subofs) {
						if(!subof.isGCI()) {
							OWLClassExpression lhs = subof.getSubClass();
							cl_supporting_new_axioms_2.put(lhs.asOWLClass(), subof);
						}
					}
				}else if(cl_supporting_1_in_witnesses_2_ax.isOfType(AxiomType.SUBCLASS_OF)) {
					OWLSubClassOfAxiom subof = (OWLSubClassOfAxiom) cl_supporting_1_in_witnesses_2_ax;
					if(!subof.isGCI()) {
						OWLClassExpression lhs = subof.getSubClass();
						cl_focus_new_axioms_2.put(lhs.asOWLClass(), subof);
					}
				}
			}
			Multimap<OWLClass, OWLAxiom> cl_supporting_old_axioms_1 = ArrayListMultimap.create();
			for(OWLAxiom cl_supporting_2_in_witnesses_1_ax: cl_supporting_2_in_witnesses_1) {
				if(cl_supporting_2_in_witnesses_1_ax.isOfType(AxiomType.EQUIVALENT_CLASSES)) {
					OWLEquivalentClassesAxiom equiv = (OWLEquivalentClassesAxiom) cl_supporting_2_in_witnesses_1_ax;
					Set<OWLSubClassOfAxiom> subofs = equiv.asOWLSubClassOfAxioms();
					for(OWLSubClassOfAxiom subof: subofs) {
						if(!subof.isGCI()) {
							OWLClassExpression lhs = subof.getSubClass();
							cl_supporting_old_axioms_1.put(lhs.asOWLClass(), equiv);
						}
					}
				}else if(cl_supporting_2_in_witnesses_1_ax.isOfType(AxiomType.SUBCLASS_OF)) {
					OWLSubClassOfAxiom subof = (OWLSubClassOfAxiom) cl_supporting_2_in_witnesses_1_ax;
					if(!subof.isGCI()) {
						OWLClassExpression lhs = subof.getSubClass();
						cl_supporting_old_axioms_1.put(lhs.asOWLClass(), subof);
					}
				}
			}
			//now we have two maps of cls and their axioms. now from these two maps, for a cl, grap its axioms from the old and add it to the set 
			//before doing that
			//aggregate the axioms that belong to a certain class in new maps
			
//			//fill the new map
//			for(OWLAxiom cl_focus_1_in_witnesses_2_ax: cl_focus_1_in_witnesses_2) {
//				if(cl_focus_1_in_witnesses_2_ax.isOfType(AxiomType.EQUIVALENT_CLASSES)) {
//					OWLEquivalentClassesAxiom equiv = (OWLEquivalentClassesAxiom) cl_focus_1_in_witnesses_2_ax;
//					Set<OWLSubClassOfAxiom> subofs = equiv.asOWLSubClassOfAxioms();
//					for(OWLSubClassOfAxiom subof: subofs) {
//						if(!subof.isGCI()) {
//							OWLClassExpression lhs = subof.getSubClass();
//							//get from the map the lhs of the current one, so that I create a new set of axioms which are corresponded to the current lhs
//							
//						}
//					}
//				}
//			}
//		
			//fill the old map
			
			//then when printing to excel, use the new map, and the old map, but the question is how to show the 
			
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet_1 = workbook.createSheet("Focus Classes");
			XSSFSheet sheet_2 = workbook.createSheet("Supporting Classes");
			Map<Integer, Object[]> focus_classes_data = new TreeMap<Integer, Object[]>();
			Map<Integer, Object[]> supporting_classes_data = new TreeMap<Integer, Object[]>();
			
			//put column headers
			int row_key_f = 1;
			focus_classes_data.put(row_key_f, new Object[]{"#","Focus Class", "Version, Focus Class Axioms"});
			
			//it would be clearer to use both sets: cl_focus_1_in_witnesses_2, and ... to put the axioms a
			Set<Integer> keyset_f = focus_classes_data.keySet();
			int rownum_f = 0;
			 for (Integer key : keyset_f) 
			    {
			        //create a row of excelsheet
			        Row row = sheet_1.createRow(rownum_f++);

			        //get object array of prerticuler key
			        Object[] objArr = focus_classes_data.get(key);

			        int cellnum = 0;

			        for (Object obj : objArr) 
			        {
			            Cell cell = row.createCell(cellnum++);
			            if (obj instanceof String) 
			            {
			                cell.setCellValue((String) obj);
			            }
			            else if (obj instanceof Integer) 
			            {
			                cell.setCellValue((Integer) obj);
			            }
			        }
			    }
			 
			 
			 
			 //create an ordered multip map that sotres the cl and its old and new axioms subsequently to be used when filling the sheets
			 //Multimap<OWLClass, String> ordered_versions_with_cls = LinkedListMultimap.create();
			 //Map<OWLClass, String> ordered_versions_with_focus_cls = new LinkedHashMap<>();
			 //Multimap<OWLClass, Collection<OWLAxiom>> ordered_cls_with_axs = LinkedListMultimap.create();
			 
			 //Multimap<OWLClass, Object> ordered_cls_with_obs = LinkedListMultimap.create();
			 //the object can be axioms or versions (strings?)
			 //for(OWLClass cl_key : total_focus_classes_2) {
				 //Collection<OWLAxiom> old_axiom_s = cl_focus_old_axioms_1.get(cl_key);
				 //Collection<OWLAxiom> new_axiom_s = cl_focus_new_axioms_2.get(cl_key);
				 
				 //ordered_versions_with_cls.put(file_1_version, cl_key);
				 //ordered_versions_with_cls.put(cl_key, file_1_version);
				 //ordered_versions_with_cls.put(cl_key, file_1_version);
				 //ordered_versions_with_cls.put(file_2_version, cl_key);
				 //ordered_versions_with_cls.put(cl_key, file_2_version);
				
				 
				 
				 //ordered_cls_with_axs.put(cl_key, old_axiom_s);
				 //ordered_cls_with_axs.put(cl_key, new_axiom_s);
				 //ordered_cls_with_obs.put(cl_key, old_axiom_s);
				 //ordered_cls_with_obs.put(cl_key, file_1_version);
				 //ordered_cls_with_obs.put(cl_key, new_axiom_s);
				 //ordered_cls_with_obs.put(cl_key, file_2_version);
			 //}
			 
			 //Map<OWLClass, Map<String, Collection<OWLAxiom>>> classes_version_axioms = new LinkedHashMap<>();
			 Multimap<OWLClass, Map<String, Collection<OWLAxiom>>> focus_classes_version_axioms = LinkedListMultimap.create();
			 for(OWLClass cl: total_focus_classes_2) {
				 
				//first old
				 Map<String, Collection<OWLAxiom>> old_version_axioms = new HashMap<>();
				 
				 Collection<OWLAxiom> old_axiom_s = cl_focus_old_axioms_1.get(cl);
				 //get from the first subontology the axiom of the current cl and 
				 
				 if(!old_axiom_s.isEmpty()) {
					 
					 
					 Collection<OWLSubClassOfAxiom> old_subof_axioms_in_subontology = Subontology_1.getSubClassAxiomsForSubClass(cl);
					 if(!old_subof_axioms_in_subontology.isEmpty()) {
					 Collection<OWLAxiom> old_subof_axioms_in_subontology_copy = new HashSet<OWLAxiom>(old_subof_axioms_in_subontology);
					 old_version_axioms.put(file_1_version + "_Axiom in Subontology", old_subof_axioms_in_subontology_copy);
					 } 
					 Collection<OWLEquivalentClassesAxiom> old_equiv_subof_axioms_in_subontology = Subontology_1.getEquivalentClassesAxioms(cl);
					 if(!old_equiv_subof_axioms_in_subontology.isEmpty()) {
						 Collection<OWLAxiom> old_equiv_subof_axioms_in_subontology_copy = new HashSet<OWLAxiom>(old_equiv_subof_axioms_in_subontology);
						 old_version_axioms.put(file_1_version + "_Axiom in Subontology", old_equiv_subof_axioms_in_subontology_copy);
						 } 
					 
					 old_version_axioms.put(file_1_version, old_axiom_s);
				 focus_classes_version_axioms.put(cl, old_version_axioms);
				 }
				 
				 //second new
				 Map<String, Collection<OWLAxiom>> new_version_axioms = new HashMap<>();
				 Collection<OWLAxiom> new_axiom_s = cl_focus_new_axioms_2.get(cl);
				 if(!new_axiom_s.isEmpty()) {
					 
					 Collection<OWLSubClassOfAxiom> new_subof_axioms_in_subontology = Subontology_2.getSubClassAxiomsForSubClass(cl);
					 if(!new_subof_axioms_in_subontology.isEmpty()) {
					 Collection<OWLAxiom> new_subof_axioms_in_subontology_copy = new HashSet<OWLAxiom>(new_subof_axioms_in_subontology);
					 new_version_axioms.put(file_2_version + "_Axiom in Subontology", new_subof_axioms_in_subontology_copy);
					 } 
					 Collection<OWLEquivalentClassesAxiom> new_equiv_subof_axioms_in_subontology = Subontology_2.getEquivalentClassesAxioms(cl);
					 if(!new_equiv_subof_axioms_in_subontology.isEmpty()) {
						 Collection<OWLAxiom> new_equiv_subof_axioms_in_subontology_copy = new HashSet<OWLAxiom>(new_equiv_subof_axioms_in_subontology);
						 new_version_axioms.put(file_2_version + "_Axiom in Subontology", new_equiv_subof_axioms_in_subontology_copy);
						 } 
					 
					 
				 new_version_axioms.put(file_2_version, new_axiom_s);
				 focus_classes_version_axioms.put(cl, new_version_axioms);
				 }
				 
			 }
			 int id_f = 0;
			 
			//we assume that the size of cls in both maps would be the same so just use one of the maps to do the iteration
				//Iterator it = cl_focus_old_axioms_1.entries().iterator();
				//Iterator it2 = cl_focus_new_axioms_2.entries().iterator();
				//this to go through the old axioms to fill the sheet, but we have another requirement,
				//we want to get the new axiom for the second row
				Iterator it_f = focus_classes_version_axioms.entries().iterator();
				//System.out.println("the content of the map: cl_focus_old_axioms_1: " + cl_focus_old_axioms_1);
				//System.out.println("the content of the map: cl_focus_new_axioms_2: " + cl_focus_new_axioms_2);
				//System.out.println("the content of the map: ordered_versions_with_cls: " + ordered_versions_with_cls);
				//System.out.println("the content of the map: ordered_cls_with_axs: " + ordered_cls_with_axs);
				//System.out.println("the content of the map: ordered_cls_with_obs: " + ordered_cls_with_obs);
				
				
				System.out.println("the content of the map: focus_classes_version_axioms: " + focus_classes_version_axioms);
				while(it_f.hasNext()) {
					id_f++;
					row_key_f++;
					//old axiom
					Entry<OWLClass, Map<String, Collection<OWLAxiom>>> entry = (Entry<OWLClass, Map<String, Collection<OWLAxiom>>>) it_f.next();
					OWLClass cl = entry.getKey();
				
					focus_classes_data.put(row_key_f, new Object[]{id_f, cl.toString(), entry.getValue().toString()} );

				
					Row row = sheet_1.createRow(rownum_f++);

			        //get object array of prerticuler key
			        Object[] objArr = focus_classes_data.get(row_key_f);

			        int cellnum = 0;

			        for (Object obj : objArr) 
			        {
			            Cell cell = row.createCell(cellnum++);
			            if (obj instanceof String) 
			            {
			                cell.setCellValue((String) obj);
			            }
			            else if (obj instanceof Integer) 
			            {
			                cell.setCellValue((Integer) obj);
			            }
			        }
				
				}
				
				
				
				int row_key_s = 1;
				 supporting_classes_data.put(row_key_s, new Object[]{"#","Supporting Class", "Version, Supporting Class Axioms"});
					
					//it would be clearer to use both sets: cl_focus_1_in_witnesses_2, and ... to put the axioms a
					Set<Integer> keyset_s = supporting_classes_data.keySet();
					int rownum_s = 0;
					 for (Integer key : keyset_s) 
					    {
					        //create a row of excelsheet
					        Row row = sheet_2.createRow(rownum_s++);

					        //get object array of prerticuler key
					        Object[] objArr = supporting_classes_data.get(key);

					        int cellnum = 0;

					        for (Object obj : objArr) 
					        {
					            Cell cell = row.createCell(cellnum++);
					            if (obj instanceof String) 
					            {
					                cell.setCellValue((String) obj);
					            }
					            else if (obj instanceof Integer) 
					            {
					                cell.setCellValue((Integer) obj);
					            }
					        }
					    }
					 

					 Multimap<OWLClass, Map<String, Collection<OWLAxiom>>> supporting_classes_version_axioms = LinkedListMultimap.create();
					 for(OWLClass cl: total_supporting_classes_2) {
						 
						//first old
						 Map<String, Collection<OWLAxiom>> old_version_axioms = new HashMap<>();
						 Collection<OWLAxiom> old_axiom_s = cl_supporting_old_axioms_1.get(cl);
						 if(!old_axiom_s.isEmpty()) {
							 
							 
							 
							 Collection<OWLSubClassOfAxiom> old_subof_axioms_in_subontology = Subontology_1.getSubClassAxiomsForSubClass(cl);
							 if(!old_subof_axioms_in_subontology.isEmpty()) {
							 Collection<OWLAxiom> old_subof_axioms_in_subontology_copy = new HashSet<OWLAxiom>(old_subof_axioms_in_subontology);
							 old_version_axioms.put(file_1_version + "_Axiom in Subontology", old_subof_axioms_in_subontology_copy);
							 } 
							 Collection<OWLEquivalentClassesAxiom> old_equiv_subof_axioms_in_subontology = Subontology_1.getEquivalentClassesAxioms(cl);
							 if(!old_equiv_subof_axioms_in_subontology.isEmpty()) {
								 Collection<OWLAxiom> old_equiv_subof_axioms_in_subontology_copy = new HashSet<OWLAxiom>(old_equiv_subof_axioms_in_subontology);
								 old_version_axioms.put(file_1_version + "_Axiom in Subontology", old_equiv_subof_axioms_in_subontology_copy);
								 } 
							 
							 
						 old_version_axioms.put(file_1_version, old_axiom_s);
						 supporting_classes_version_axioms.put(cl, old_version_axioms);
						 }
						 
						 
						 //second new
						 Map<String, Collection<OWLAxiom>> new_version_axioms = new HashMap<>();
						 Collection<OWLAxiom> new_axiom_s = cl_supporting_new_axioms_2.get(cl);
						 if(!new_axiom_s.isEmpty()) {
							 
							 Collection<OWLSubClassOfAxiom> new_subof_axioms_in_subontology = Subontology_2.getSubClassAxiomsForSubClass(cl);
							 if(!new_subof_axioms_in_subontology.isEmpty()) {
							 Collection<OWLAxiom> new_subof_axioms_in_subontology_copy = new HashSet<OWLAxiom>(new_subof_axioms_in_subontology);
							 new_version_axioms.put(file_2_version + "_Axiom in Subontology", new_subof_axioms_in_subontology_copy);
							 } 
							 Collection<OWLEquivalentClassesAxiom> new_equiv_subof_axioms_in_subontology = Subontology_2.getEquivalentClassesAxioms(cl);
							 if(!new_equiv_subof_axioms_in_subontology.isEmpty()) {
								 Collection<OWLAxiom> new_equiv_subof_axioms_in_subontology_copy = new HashSet<OWLAxiom>(new_equiv_subof_axioms_in_subontology);
								 new_version_axioms.put(file_2_version + "_Axiom in Subontology", new_equiv_subof_axioms_in_subontology_copy);
								 }
							 
							 
						 new_version_axioms.put(file_2_version, new_axiom_s);
						 supporting_classes_version_axioms.put(cl, new_version_axioms);
						 }
						
					 }
					 
					 
					 int id_s = 0;
					 
					//we assume that the size of cls in both maps would be the same so just use one of the maps to do the iteration
						//Iterator it = cl_focus_old_axioms_1.entries().iterator();
						//Iterator it2 = cl_focus_new_axioms_2.entries().iterator();
						//this to go through the old axioms to fill the sheet, but we have another requirement,
						//we want to get the new axiom for the second row
						Iterator it_s = supporting_classes_version_axioms.entries().iterator();
						//System.out.println("the content of the map: cl_focus_old_axioms_1: " + cl_focus_old_axioms_1);
						//System.out.println("the content of the map: cl_focus_new_axioms_2: " + cl_focus_new_axioms_2);
						//System.out.println("the content of the map: ordered_versions_with_cls: " + ordered_versions_with_cls);
						//System.out.println("the content of the map: ordered_cls_with_axs: " + ordered_cls_with_axs);
						//System.out.println("the content of the map: ordered_cls_with_obs: " + ordered_cls_with_obs);
						
						
						System.out.println("the content of the map: supporting_classes_version_axioms: " + supporting_classes_version_axioms);
						while(it_s.hasNext()) {
							id_s++;
							row_key_s++;
							//old axiom
							Entry<OWLClass, Map<String, Collection<OWLAxiom>>> entry = (Entry<OWLClass, Map<String, Collection<OWLAxiom>>>) it_s.next();
							OWLClass cl = entry.getKey();
						
							supporting_classes_data.put(row_key_s, new Object[]{id_s, cl.toString(), entry.getValue().toString()} );

						
							Row row = sheet_2.createRow(rownum_s++);

					        //get object array of prerticuler key
							int cellnum = 0;
					        Object[] objArr = supporting_classes_data.get(row_key_s);
					       // if(objArr != null) {
					        

					        for (Object obj : objArr) 
					        {
					            Cell cell = row.createCell(cellnum++);
					            if (obj instanceof String) 
					            {
					                cell.setCellValue((String) obj);
					            }
					            else if (obj instanceof Integer) 
					            {
					                cell.setCellValue((Integer) obj);
					            }
					        }
					        }
						
						//}	 
					 
				
				try 
			    {
			    FileOutputStream out = new FileOutputStream(new File("diff_analyser_results_" + file_1_version + "_" + file_2_version + ".xlsx"));
			    workbook.write(out);
			    out.close();
			    } catch (Exception e)
			    {
			        e.printStackTrace();
			    }
			
		}
		
		
	
		//Create an axiom to check if it's entailed by a ontology
		public void create_axiom(String filePath) throws OWLOntologyCreationException {
			
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			
			File file1 = new File(filePath);
			IRI iri1 = IRI.create(file1);
			OWLOntology O = manager.loadOntologyFromOntologyDocument(new IRIDocumentSource(iri1),
					new OWLOntologyLoaderConfiguration().setLoadAnnotationAxioms(true));
			
			
			System.out.println("the O axioms size: " + O.getLogicalAxiomCount());
			System.out.println("the O classes size: " + O.getClassesInSignature().size());
			System.out.println("the O properties size: " + O.getObjectPropertiesInSignature().size());
			
			
			
			OWLOntologyManager manager1 = OWLManager.createOWLOntologyManager();
			OWLDataFactory df = manager1.getOWLDataFactory();
			
			OWLClass lhs_cl = df.getOWLClass(IRI.create("http://snomed.info/id/699853005"));
			OWLClass rhs_cl_1 = df.getOWLClass(IRI.create("http://snomed.info/id/43162008"));
			OWLObjectProperty role_group = df.getOWLObjectProperty(IRI.create("http://snomed.info/id/609096000"));
			OWLObjectProperty rhs_pr_1 = df.getOWLObjectProperty(IRI.create("http://snomed.info/id/116676008"));
			OWLClass rhs_cl_fl_1 = df.getOWLClass(IRI.create("http://snomed.info/id/416286003"));
			OWLObjectSomeValuesFrom obsv_1 = df.getOWLObjectSomeValuesFrom(rhs_pr_1, rhs_cl_fl_1);
			OWLObjectProperty rhs_pr_2 = df.getOWLObjectProperty(IRI.create("http://snomed.info/id/246454002"));
			OWLClass rhs_cl_fl_2 = df.getOWLClass(IRI.create("http://snomed.info/id/255399007"));
			OWLObjectSomeValuesFrom obsv_2 = df.getOWLObjectSomeValuesFrom(rhs_pr_2, rhs_cl_fl_2);
			OWLObjectProperty rhs_pr_3 = df.getOWLObjectProperty(IRI.create("http://snomed.info/id/363698007"));
			OWLClass rhs_cl_fl_3 = df.getOWLClass(IRI.create("http://snomed.info/id/419500006"));
			OWLObjectSomeValuesFrom obsv_3 = df.getOWLObjectSomeValuesFrom(rhs_pr_3, rhs_cl_fl_3);
			OWLObjectProperty rhs_pr_4 = df.getOWLObjectProperty(IRI.create("http://snomed.info/id/370135005"));
			OWLClass rhs_cl_fl_4 = df.getOWLClass(IRI.create("http://snomed.info/id/308490002"));
			OWLObjectSomeValuesFrom obsv_4 = df.getOWLObjectSomeValuesFrom(rhs_pr_4, rhs_cl_fl_4);
			
			Set<OWLClassExpression> obsv_fillers = new HashSet<>();
			obsv_fillers.add(obsv_1);
			obsv_fillers.add(obsv_2);
			obsv_fillers.add(obsv_3);
			obsv_fillers.add(obsv_4);
			
			OWLObjectIntersectionOf object_intersection = df.getOWLObjectIntersectionOf(obsv_fillers);
			OWLObjectSomeValuesFrom obsv = df.getOWLObjectSomeValuesFrom(role_group, object_intersection);
			Set<OWLClassExpression> rhs_exp = new HashSet<>();
			rhs_exp.add(obsv);
			rhs_exp.add(rhs_cl_1);
			OWLObjectIntersectionOf object_inter_rhs = df.getOWLObjectIntersectionOf(rhs_exp);
			OWLSubClassOfAxiom gci_subof = df.getOWLSubClassOfAxiom(object_inter_rhs, lhs_cl);
			
			//check the axiom
			
			System.out.println("the created ax: gci_subof: " + gci_subof);
			
			if(checkEntailement(gci_subof, O)){
				System.out.println("the gci direction is not a witness");
			}
		}
	
		public boolean checkEntailement(OWLLogicalAxiom logicalAxiom, OWLOntology o) {
			//OWLReasonerFactory fac = new Reasoner.ReasonerFactory();
			//OWLReasoner hermit_reasoner = fac.createReasoner(o);
			Configuration c = new Configuration();
			OWLReasoner reasoner = new Reasoner(c,o);
			if(reasoner.isEntailed(logicalAxiom)) {
				System.out.println("The axiom is entailed by O");
				reasoner.dispose();
				return true;
			}else {
				System.out.println("The axiom is NOT entailed by O");
				reasoner.dispose();
				return false;
			}
		}
	
	public static void main(String[] args)
			throws Exception {
		
		
		
		
		System.out.println("--- Analysing the differences between ontologies ---"); 
		//String filePath1 = args[0];
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/Subontologies/era_sct_intl_20200904_new_IRI.owl_snomed_ct_australian.owl_20171231-before-grouping-subontology-v.14.7.owl";		
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-sct-intl201907-intl201901/witness_complete_1.owl";
		
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/data-source/sct_intl_20190131.owl";
		
		
		
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-comparisons-module-common-sig/ERA-sct-intl201707-intl201701/ERA-sct-intl201707-intl201701/witness_complete_2.owl";
		
		//MRI
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/MRI/MRI-sct-intl201801-intl201707/MRI-sct-intl201801-intl201707/witness_complete_2.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/Abstracted_def_based_subontologies/computed-sub-ontologies/updated-subontologies/era_sct_intl_20200904_new_IRI.owl_sct-international_20170731-subontology.owl";
		//System.out.println("--------------Witnesses Ontology 1 file name: " + filePath1 +"--------------"); 
		//String filePath2 = args[1];
		//String filePath2 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-sct-intl201901-intl201807/witness_complete_1.owl";
		//String filePath2 = "/Users/ghadahalghamdi/Documents/Abstracted_def_based_subontologies/computed-sub-ontologies/updated-subontologies/era_sct_intl_20200904_new_IRI.owl_snomed_ct_australian.owl_20171231-subontology-v.14.7.owl";
		
		//MRI
		//String filePath2 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/MRI/MRI-sct-intl201801-intl201707/MRI-sct-intl201801-intl201707/witness_complete_1.owl";
		//String filePath2 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-comparisons-module-common-sig/ERA-sct-intl201707-intl201701/ERA-sct-intl201707-intl201701/witness_complete_1.owl";
		//System.out.println("--------------Witnesses Ontology 2 file name: " + filePath2 +"--------------"); 
		
		
		//String filePath3 = args[2];
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/Subontologies/era_sct_intl_20200904_new_IRI.owl_snomed_ct_australian.owl_20171231-before-grouping-subontology-v.14.7.owl";		
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-sct-intl201907-intl201901/witness_complete_1.owl";
		
		
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-comparisons-module-common-sig/ERA-sct-intl201707-intl201701/ERA-sct-intl201707-intl201701/witness_complete_2.owl";
		
		//MRI
		//String filePath1 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/MRI/MRI-sct-intl201801-intl201707/MRI-sct-intl201801-intl201707/witness_complete_2.owl";
		//String filePath1 = "/Users/ghadahalghamdi/Documents/Abstracted_def_based_subontologies/computed-sub-ontologies/updated-subontologies/era_sct_intl_20200904_new_IRI.owl_sct-international_20170731-subontology.owl";
		//System.out.println("--------------SubOntology 1 file name: " + filePath3 +"--------------"); 
		//String filePath4 = args[3];
		//String filePath2 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-sct-intl201901-intl201807/witness_complete_1.owl";
		//String filePath2 = "/Users/ghadahalghamdi/Documents/Abstracted_def_based_subontologies/computed-sub-ontologies/updated-subontologies/era_sct_intl_20200904_new_IRI.owl_snomed_ct_australian.owl_20171231-subontology-v.14.7.owl";
		
		//MRI
		//String filePath2 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/MRI/MRI-sct-intl201801-intl201707/MRI-sct-intl201801-intl201707/witness_complete_1.owl";
		//String filePath2 = "/Users/ghadahalghamdi/Documents/IJCAI2021-results/sct-subontologies-comparisons/ERA/ERA-comparisons-module-common-sig/ERA-sct-intl201707-intl201701/ERA-sct-intl201707-intl201701/witness_complete_1.owl";
		//System.out.println("--------------SubOntology 2 file name: " + filePath4 +"--------------"); 
		
		
		//GO slim mouse
		String filePath1 = "/Users/ghadahalghamdi/Documents/UI_diff_tools/UI_diff_goslim_mouse_view_subontology/witness_complete_1_subontology.owl";
		String filePath2 = "/Users/ghadahalghamdi/Documents/UI_diff_tools/UI_diff_goslim_mouse_view_subontology/witness_complete_2_view.owl";
		String filePath3 = "/Users/ghadahalghamdi/Documents/Ontologies_evaluation_chapter/Go-ontology/Comparison-1/goslim_mouse.owl_go.owl_denormalised.owl_01012021_denormalised-before-grouping-subontology-v.14.10.owl";
		String filePath4 = "/Users/ghadahalghamdi/Documents/Ontologies_evaluation_chapter/Go-ontology/Comparison-1/goslim_mouse.owl_go.owl_denormalised.owl_01012021_denormalised-before-grouping-subontology-v.14.10.owl";
		String o_1_version = "01012021_denormalised_goslim_mouse";
		String o_2_version = "01012021_denormalised_goslim_mouse";
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
		UI_diff_analysis_2 a = new UI_diff_analysis_2();
		//diff.using_ui_diff(filePath1, filePath2, filePath3);
		a.analyse_diffs(filePath1, filePath2, filePath3, filePath4, o_1_version, o_2_version);
		//a.create_axiom(filePath1);
		long endTime1 = System.currentTimeMillis();
		System.out.println("Total Duration = " + (endTime1 - startTime1) + "millis");







	}

}
