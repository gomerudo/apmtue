/**
 * Author: Jorge Gómez (j.gomez.robles@student.tue.nl) 
 * 
 * This script is used as an utility for the Assignment 1 of Advanced Process Mining.
 * Its purpose is to get some information from the event log to use it as source of
 * comparaison for the question in the assignment.
 */

def assignmentDirectory = "/Users/gomerudo/Documents/Master/quartile2/2IMI20/assignment1"
def logName = "APM_Assignment_1.xes"

// Parse log event
def hospital = new XmlParser().parse(new File("${assignmentDirectory}/${logName}"))

// Node with all the events in the log
def events = hospital.trace.event

// Get all the resources in the log and print them
def allResources = getResources(events)
println "Number of resources: ${allResources.size()}"
printMap allResources

// Get all the concepts in the log and print them
def allConcepts = getConcepts(events)
println "Number of concepts: ${allConcepts.size()}"
printMap allConcepts

// Get all the tasks for each resource, i.e, the tasks a given resource works on
allResources.each{ name, freq ->
    def tasksForResource = getTasksFor(name, events)
    printMap tasksForResource
}

/** 
  *  Function to get all the tasks where a given resource appears.
  */
def getTasksFor(name, eventsNode){
    def filter = eventsNode.findAll{ node ->
        node.string.@value.contains(name)
    }

    def taskMap = [:]
    filter.each{ node -> 
        node.string.each { str ->
            if( str.@key == "concept:name" ){
                def customKey = "${name}-${str.@value}"
                if( taskMap.containsKey( customKey ) ){
                    taskMap.put( customKey  , taskMap.get(customKey) + 1 )   
                }
                else{
                    taskMap.put(customKey, 1 )
                }

            }
        }
    }
    return taskMap 
}

/** 
  *  Function to get all the resources on the node.
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
  *  Function to get all the concepts (i.e. type of event) on the node.
  */
def getConcepts(eventsNode){
    def filter = eventsNode.'**'.findAll{ node ->
        node.string.@key.contains("concept:name")
    }

    def resourcesMap = [:]
    filter.each{ node -> 
        node.string.each { str ->
            if( str.@key == "concept:name" ){
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
  * Function to print a Map object
  */
def printMap(map){
    map.each{ key, value ->
        println "${key} : ${value}"

    }
}