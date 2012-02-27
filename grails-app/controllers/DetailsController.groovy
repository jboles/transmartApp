/*************************************************************************
  * tranSMART - translational medicine data mart
 * 
 * Copyright 2008-2012 Janssen Research & Development, LLC.
 * 
 * This product includes software developed at Janssen Research & Development, LLC.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software  * Foundation, either version 3 of the License, or (at your option) any later version, along with the following terms:
 * 1.	You may convey a work based on this program in accordance with section 5, provided that you retain the above notices.
 * 2.	You may convey verbatim copies of this program code as you receive it, in any medium, provided that you retain the above notices.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS    * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *
 ******************************************************************/
/*
 * $Id: DetailsController.groovy 11850 2012-01-24 16:41:12Z jliu $
 */
import grails.converters.JSON

/**
 * This controller is used to display details data for genes, pathways, and compounds. For each of these there
 * is a corresponsing action and view which displays a window with tabs, e.g. "gene" action and view. And then
 * if a summary is define a summary action and view, e.g. compoundSumary action and view.
 *
 * @author $Author: jliu $
 * @version $Revision: 11850 $
 */
class DetailsController {

	def detailsService

	def gene = {
		def bioDataId = params?.id
		def altId = params?.altId
		def geneSymbol = ""
		def uniqueId = ""
		def geneId = ""
		if ((bioDataId == null || bioDataId.length() ==  0) && (altId != null & altId.length() > 0)) {
			// TODO: Add type criteria
			def result = bio.BioMarker.findByPrimaryExternalId(altId)
			if (result != null) {
				bioDataId = result.id.toString()
			}
		}
		if (bioDataId != null && bioDataId.length() > 0) {
			def marker = bio.BioMarker.get(Long.valueOf(bioDataId))
		//	def searchKeyword = search.SearchKeyword.findByBioDataId(Long.valueOf(bioDataId))
		//	if (searchKeyword != null) {
		//		geneSymbol = searchKeyword.keyword
		//		uniqueId = searchKeyword.uniqueId
		//		geneId = uniqueId.substring(uniqueId.indexOf(":") + 1)
		//	}
			if(marker!=null){
				geneSymbol = marker.name;
				geneId = marker.primaryExternalId;
			}
		}
		
	
		render(view:"gene", model:[id:bioDataId, symbol:geneSymbol, geneId:geneId])
	}

	def pathway = {
		def bioDataId = params.id
		def pathwaySymbol = ""
		def uniqueId = ""
		def pathwayType = ""
		def searchKeyword = search.SearchKeyword.findByBioDataId(Long.valueOf(bioDataId))
		if (searchKeyword != null) {
			pathwaySymbol = searchKeyword.keyword
			uniqueId = searchKeyword.uniqueId
			pathwayType = uniqueId.substring(uniqueId.indexOf(":") + 1, uniqueId.lastIndexOf(":"))
		}
		render(view:"pathway", model:[id:bioDataId, symbol:pathwaySymbol, type:pathwayType])
	}

	def pathwaySummary = {
		def bioDataId = params.id
		def pathway = bio.BioMarker.get(Long.valueOf(bioDataId))
		def genes
		if (pathway != null) {
			def query = "select k from search.SearchKeyword k, bio.BioDataCorrelation c where k.bioDataId=c.associatedBioDataId and c.bioDataId=?"
			genes = search.SearchKeyword.executeQuery(query, Long.valueOf(bioDataId))
		}
		render(view:"pathwaySummary", model:[pathway:pathway,genes:genes])
	}

	def compound = {
		def bioDataId = params.id
		def compoundSymbol = ""
		def uniqueId = ""
		def searchKeyword = search.SearchKeyword.findByBioDataId(Long.valueOf(bioDataId))
		if (searchKeyword != null) {
			compoundSymbol = searchKeyword.keyword
			uniqueId = searchKeyword.uniqueId
		}
		render(view:"compound", model:[id:bioDataId, symbol:compoundSymbol])
	}

	def compoundSummary = {
		def bioDataId = params.id
		def compound = bio.Compound.get(Long.valueOf(bioDataId))
		render(view:"compoundSummary", model:[compound:compound])
	}
}