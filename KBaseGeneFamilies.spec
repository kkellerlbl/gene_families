module KBaseGeneFamilies {

	typedef string pfm_name;

	/* 
		@id ws KBaseGeneFamilies.ProteinFamilyModel
	*/
	typedef string pfm_ref;

	/*
		pfm_name family_name - protein family model name
		string family_type - type like 'pfam', 'tigrfam', ... (we need to register enum of these values somewhere)
		string description - short description like domain functional role
		int is_cdd - if 1 then next cdd fields should be used for search
		string cdd_scoremat_file - main file used in RPS-blast
		string cdd_consensus_seq - consensus of family multiple alignment
		double cdd_threshold - threshold for RPS-blast (default value is 9.82)
		string cdd_rps_blast_version - now we support RPS-blast version 2.2.29		
		string cdd_revision_date - now the last cdd revision date is 2014-02-20
	*/
	typedef structure {
		pfm_name family_name;
		string description;
		int is_cdd;
		string cdd_scoremat_gzip_file; 
		string cdd_consensus_seq;
		float cdd_threshold;
		string cdd_rps_blast_version;
		string cdd_revision_date;
	} ProteinFamilyModel;

	/*
		string set_name - name of model set
		mapping<pfm_name, pfm_ref> data - mapping from family name to reference to family object
	*/
	typedef structure {
		string set_name;
		mapping<pfm_name, pfm_ref> data;
	} ProteinFamilyModelSet;

	/* 
		@id ws KBaseGenomes.Genome
	*/
	typedef string genome_ref;

	/* 
		@id ws KBaseGeneFamilies.ProteinFamilyCluster
	*/
	typedef string pfc_ref;

	/*
		pfm_ref model - reference to protein family model
		pfc_ref parent_ref - optional reference to parent cluster (containing data describing some common set of genomes)
		list<tuple<genome_ref,string contig_id,string feature_id,int feature_start,int feature_stop,int start_in_feature,int stop_in_feature,float evalue,float bitscore>> data - 
			list of entrances of this family into different genomes
		@optional parent_ref
	*/
	typedef structure {
		pfm_ref model;
		pfc_ref parent_ref;
		list<tuple<genome_ref,string feature_id,string contig_id,int feature_start,int feature_stop,int start_in_feature,int stop_in_feature,float evalue,float bitscore>> data;
	} ProteinFamilyCluster;

	typedef string contig_id;

	/*
		genome_ref genome - reference to genome
		mapping<contig_id, list<tuple<string feature_id,int feature_start,int feature_stop,list<tuple<pfm_ref,int start_in_feature,int stop_in_feature,float evalue,float bitscore>>>>> data - 
			list of entrances of different protein families into proteins of this genome
	*/
	typedef structure {
		genome_ref genome;
		mapping<contig_id, list<tuple<string feature_id,int feature_start,int feature_stop,list<tuple<pfm_ref,int start_in_feature,int stop_in_feature,float evalue,float bitscore>>>>> data;
	} ProteinFamilyAnnotation;

	/* 
		@id ws KBaseGeneFamilies.ProteinFamilyAnnotation
	*/
	typedef string pfa_ref;

	/* 
		@id ws KBaseGeneFamilies.ProteinFamiliesSearchResult
	*/
	typedef string pfsr_ref;

	/*
		pfsr_ref parent_ref - optional reference to parent cluster search results
		mapping<genome_ref, ProteinFamilyAnnotation> genomes - genomes that user passed as input data for protein family search
		mapping<genome_ref, pfa_ref> genome_refs - genome references in case we don't want to store genomes themselves
		mapping<pfm_ref, ProteinFamilyCluster> pf_clusters - clusters constructed based on query_genomes plus genomes from parent object
		mapping<pfm_ref, pfc_ref> pf_cluster_refs - references to clusters in case we don't want to store clusters themselves
		@optional parent_ref
	*/
	typedef structure {
		pfsr_ref parent_ref;
		mapping<genome_ref, ProteinFamilyAnnotation> genomes;
		mapping<genome_ref, pfa_ref> genome_refs;
		mapping<pfm_ref, ProteinFamilyCluster> pf_clusters;
		mapping<pfm_ref, pfc_ref> pf_cluster_refs;
	} ProteinFamiliesSearchResult;

	/*
		list<genome_ref> genomes - genome list
		list<string> family_types - type list (if empty list all types are used)
		string out_workspace - output workspace
		string out_result_id - id of resulting object of type ProteinFamiliesSearchResult
	*/
	typedef structure {
		list<genome_ref> genomes;
		list<string> family_types;
		string out_workspace;
		string out_result_id;
	} search_protein_families_params;

	funcdef search_protein_families(search_protein_families_params input) returns (string job_id) authentication required;

};