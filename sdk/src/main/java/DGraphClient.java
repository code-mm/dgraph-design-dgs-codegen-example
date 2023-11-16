import com.my.dgraph.types.client.GetDDeviceGraphQLQuery;
import com.my.dgraph.types.client.QueryDDeviceGraphQLQuery;
import com.my.dgraph.types.client.QueryDDeviceProjectionRoot;
import com.my.dgraph.types.types.DDeviceFilter;
import com.my.dgraph.types.types.StringHashFilter_StringRegExpFilter;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;


import java.util.List;
import java.util.TreeSet;

public class DGraphClient {

    public static void main(String[] args) {
        String query =
                new GraphQLQueryRequest(new QueryDDeviceGraphQLQuery(
                        DDeviceFilter.newBuilder().androidIds(StringHashFilter_StringRegExpFilter.newBuilder().in(List.of("a")).build())
                        .build(), 1,0,null, new TreeSet<>()),
                        new QueryDDeviceProjectionRoot<>()
                                .deviceId()).serialize();

        System.out.println(query);
    }
}
