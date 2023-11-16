package com.my.app


fun getGQLSchema():String{
    return """
 
        query{
          getGQLSchema {
            id
            schema
            generatedSchema
          }
        }

    """.trimIndent()
}

fun DgraphIndex():String{
    return """
        enum DgraphIndex {
        	int
        	int64
        	float
        	bool
        	hash
        	exact
        	term
        	fulltext
        	trigram
        	regexp
        	year
        	month
        	day
        	hour
        	geo
        }
    """.trimIndent()
}

fun update(schema: String): String {
    return """
        mutation {
          updateGQLSchema(
            input: { set: { schema: "${schema}"}})
          {
            gqlSchema {
              schema
              generatedSchema
            }
          }
        }
    """.trimIndent()
}

fun export():String{
    return """
        mutation {
          export(input: { format: "json" }) {
            response {
              code
              message
            }
            taskId
          }
        }
    """.trimIndent()
}