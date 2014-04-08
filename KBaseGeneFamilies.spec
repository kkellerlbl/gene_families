module KBaseGeneFamilies {

	typedef string domain_name;

	/* 
		@id ws KBaseGeneFamilies.DomainModel
	*/
	typedef string domain_model_ref;

	/*
		domain_name domain_name - domain model name
		string domain_type - type is one of 'CHL', 'COG', 'KOG', 'LOAD', 'MTH', 'PHA', 'PLN', 'PRK', 'PTZ', 
			'TIGR', 'cd', 'pfam', 'smart'. 
		string description - short description like domain functional role
		int is_full_length - if 1 then there could be found only 1 domain copy of this type in protein
		int is_cdd - if 1 then next cdd fields should be used for search
		string cdd_scoremat_file - main file used in RPS-blast
		string cdd_consensus_seq - consensus of domain multiple alignment
		double cdd_threshold - threshold for RPS-blast (default value is 9.82)
		string cdd_rps_blast_version - now we support RPS-blast version 2.2.29		
		string cdd_revision_date - now the last cdd revision date is 2014-02-20
		@optional cdd_scoremat_gzip_file
		@optional cdd_consensus_seq
		@optional cdd_threshold
		@optional cdd_rps_blast_version
		@optional cdd_revision_date
	*/
	typedef structure {
		domain_name domain_name;
		string domain_type;
		string description;
		int is_full_length;
		int is_cdd;
		string cdd_scoremat_gzip_file; 
		string cdd_consensus_seq;
		float cdd_threshold;
		string cdd_rps_blast_version;
		string cdd_revision_date;
	} DomainModel;

	/* 
		@id ws KBaseGeneFamilies.DomainModelSet
	*/
	typedef string dms_ref;

	/*
		string set_name - name of model set
		mapping<domain_name, domain_model_ref> data - mapping from domain name to reference to domain model object
	*/
	typedef structure {
		string set_name;
		mapping<domain_name, domain_model_ref> data;
	} DomainModelSet;

	/* 
		@id ws KBaseGenomes.Genome
	*/
	typedef string genome_ref;

	/* 
		@id ws KBaseGeneFamilies.DomainCluster
	*/
	typedef string domain_cluster_ref;

	/*
		domain_model_ref model - reference to domain model
		domain_cluster_ref parent_ref - optional reference to parent cluster (containing data describing some common set of genomes)
		mapping<genome_ref,list<tuple<string contig_id,string feature_id,int feature_list_pos,int number_of_copies,float best_evalue,
			float best_bitscore,string best_profile_alignment>>> data - list of entrances of this domain into different genomes
		@optional parent_ref
	*/
	typedef structure {
		domain_model_ref model;
		domain_cluster_ref parent_ref;
		mapping<genome_ref,list<tuple<string contig_id,string feature_id,int feature_list_pos,int number_of_copies,float best_evalue,float best_bitscore,string best_profile_alignment>>> data;
	} DomainCluster;

	typedef string contig_id;

	/*
		genome_ref genome - reference to genome
		mapping<contig_id, list<tuple<string feature_id,int feature_start,int feature_stop,list<tuple<domain_model_ref,int start_in_feature,int stop_in_feature,float evalue,float bitscore>>>>> data - 
			list of entrances of different domains into proteins of this genome
	*/
	typedef structure {
		genome_ref genome;
		mapping<contig_id, list<tuple<string feature_id,int feature_start,int feature_stop,list<tuple<domain_model_ref,int start_in_feature,int stop_in_feature,float evalue,float bitscore>>>>> data;
	} DomainAnnotation;

	/* 
		@id ws KBaseGeneFamilies.DomainAnnotation
	*/
	typedef string domain_annotation_ref;

	/* 
		@id ws KBaseGeneFamilies.DomainSearchResult
	*/
	typedef string dsr_ref;

	/*
		dsr_ref parent_ref - optional reference to parent domain clusters search results
		dms_ref used_dms_ref - domain models used for search
		mapping<genome_ref, DomainAnnotation> annotations - found domains in genomes that user defined as input data for domain search
		mapping<genome_ref, domain_annotation_ref> annotation_refs - domain annotation references in case we don't want to store it inside search result object
		mapping<domain_model_ref, DomainCluster> domain_clusters - clusters constructed based on query_genomes plus genomes from parent object
		mapping<domain_model_ref, domain_cluster_ref> domain_cluster_refs - references to clusters in case we don't want to store these clusters inside search result object
		@optional parent_ref
		@optional annotations
		@optional annotation_refs
		@optional domain_clusters
		@optional domain_cluster_refs
	*/
	typedef structure {
		dsr_ref parent_ref;
		dms_ref used_dms_ref;
		mapping<genome_ref, DomainAnnotation> annotations;
		mapping<genome_ref, domain_annotation_ref> annotation_refs;
		mapping<domain_model_ref, DomainCluster> domain_clusters;
		mapping<domain_model_ref, domain_cluster_ref> domain_cluster_refs;
	} DomainSearchResult;

	/*
		list<genome_ref> genomes - genome list
		dms_ref dms_ref - set of domain models that will be searched in defined genomes (optional field, you can use clusters_for_extension instead)
		dsr_ref clusters_for_extension - clusters already constructed for another set of genomes (public ones for example)
		list<string> domain_types - type list (if empty list all types are used) defining subset of domain models extracted from dms_ref/clusters_for_extension
		string out_workspace - output workspace
		string out_result_id - id of resulting object of type DomainSearchResult
		int is_genome_annotation_stored_outside - default value is 0
		string genome_annotation_id_prefix - used for genome domain annotation objects id generation ([prefix.]genome_name[.suffix])
		string genome_annotation_id_suffix - used for genome domain annotation objects id generation ([prefix.]genome_name[.suffix])
		int is_domain_cluster_data_stored_outside - default value is 0
		string domain_cluster_data_id_prefix - used for domain cluster objects id generation ([prefix.]domain_name[.suffix])
		string domain_cluster_data_id_suffix - used for domain cluster objects id generation ([prefix.]domain_name[.suffix])
		@optional dms_ref
		@optional clusters_for_extension
		@optional is_genome_annotation_stored_outside
		@optional genome_annotation_id_prefix
		@optional genome_annotation_id_suffix
		@optional is_domain_cluster_data_stored_outside
		@optional domain_cluster_data_id_prefix
		@optional domain_cluster_data_id_suffix
	*/
	typedef structure {
		list<genome_ref> genomes;
		dms_ref dms_ref;
		dsr_ref clusters_for_extension;
		list<string> domain_types;
		string out_workspace;
		string out_result_id;
		int is_genome_annotation_stored_outside;
		string genome_annotation_id_prefix;
		string genome_annotation_id_suffix;
		int is_domain_cluster_data_stored_outside;
		string domain_cluster_data_id_prefix;
		string domain_cluster_data_id_suffix;
	} search_domains_params;

	funcdef search_domains(search_domains_params input) returns (string job_id) authentication required;

};