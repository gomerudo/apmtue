/**
 * HOW TO USE THIS SCRIPT.
 * 
 * This script looks for the event logs in the rootDir variable and can perform
 * the next operations:
 *      - Get basic information: uncomment line 23
 *      - Filter event log: uncomment line 24
 *      - Get resources in the event logs: Uncomment lines  27, 30-32
 *      - Get all the concept names in the event logs (all activities): uncomment lines 34, 37
 */

import groovy.xml.*

def rootDir = "/Users/gomerudo/Documents/Master/quartile2/2IMI20/assignment3/logs"

def allActivities = [:]

for(int i = 1; i < 6; i++){

    def a3 = new XmlParser().parse(new File("${rootDir}/BPIC15_${i}_CLEAN.xes_FILTERED.xes"))

    def traces = a3.trace
    //getBasicInfo(traces)
    //filtering(traces)

    // Uncomment for methods below
    //def events = a3.trace.event

    // Get  Resources 
    // def allResources = getResources(events)
    // println "Number of resources: ${allResources.size()}"
    // printMap allResources

    // getConcepts(events, allActivities)
}

//printMap allActivities


/**
    Get basic information.
*/
def filtering(traces){
    // def filtered = filterEventLog(traces)
    // filtered.each{
    //     a3.remove(it)
    // }
        
    // def nodeAsText = XmlUtil.serialize(a3)
    // File aux = new File("${rootDir}/BPIC15_${i}_CLEAN.xes")
    // aux << nodeAsText
}


/**
    Get basic information.
*/
def getBasicInfo(tracesNode){
    println "Number of cases: ${tracesNode.size()}"
    println "Total number of events: ${tracesNode.event.size()}"
}

/**
    Filter cases
*/
def filterEventLog(tracesNode){
    def auxNode
    def filter = tracesNode.'**'.findAll{ node ->
        def boolFlag = true
        def events = node.event
        events.each{ el ->
            el.string.each{ str ->
                if( str.@key == "concept:name" && str.@value == "01_HOOFD_515" ){
                    boolFlag = false
                    auxNode = node
                }
            }
        }
        
        if( boolFlag ){
            tracesNode.remove(auxNode)
        }
        boolFlag
    }
    
    return tracesNode
}


/** 
    Function to get all the resources.
*/
def getResources(eventsNode){
    def filter = eventsNode.'**'.findAll{ node ->
        node.string.@key.contains("org:resource")
    }

    def resourcesMap = [:]
    filter.each{ node -> 
        node.string.each { str ->
            if( str.@key == "org:resource" ){
                if( resourcesMap.containsKey( str.@value ) ){
                    resourcesMap.put( str.@value  , resourcesMap.get(str.@value) + 1 )   
                }
                else{
                    resourcesMap.put(str.@value, 1 )
                }
            }
        }
    }
    return resourcesMap 
}

/**
 Get "by law" approvals.
 **/
// def getCasesApprovedByLaw(eventsNode){
//     def filter = eventsNode.'**'.findAll{ node ->
//         node.string.@key.contains("concept:name")
//     }

//     def actID = null
//     def actEN = null

//     def count = 0

//     filter.each{ node -> 
//         node.string.each { str ->
//             if( str.@key == "concept:name" && str.@value == "01_HOOFD_480" ){
//                 count++
//             }
//         }
//     }
//     return count 
// }


/** 
 Function to get all the resources.
 **/
def getConcepts(eventsNode, resourcesMap){
    def filter = eventsNode.'**'.findAll{ node ->
        node.string.@key.contains("concept:name")
    }

    def actID = null
    def actEN = null

    filter.each{ node -> 
        node.string.each { str ->
            if( str.@key == "concept:name" ){
                actID = str.@value
            }
            if( str.@key == "activityNameEN" ){
                actEN = str.@value
            }
        }

        if( actID != null && actEN != null ){
            resourcesMap.put( actID, actEN )   
        }
        actID = null
        actEN = null
    }
    return resourcesMap 
}


def printMap(map){
    map.each{ key, value ->
        println "${key} : ${value}"

    }
}