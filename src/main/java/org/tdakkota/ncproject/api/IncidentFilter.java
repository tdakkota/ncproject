package org.tdakkota.ncproject.api;

import io.quarkus.panache.common.Parameters;

import javax.ws.rs.QueryParam;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IncidentFilter {
    @QueryParam("assigneeID")
    private Long assigneeID;

    @QueryParam("areaID")
    private Long areaID;

    @QueryParam("start")
    private Long start;

    @QueryParam("due")
    private Long due;

    public Map<String, Parameters> filters() {
        Map<String, Parameters> filters = new HashMap<>();
        Parameters p = new Parameters();

        if (assigneeID != null) {
            p.and("assigneeID", assigneeID);
            filters.put("byAssigneeID", p);
        }
        if (areaID != null) {
            p.and("areaID", areaID);
            filters.put("byAreaID", p);
        }
        if (start != null) {
            p.and("start", new Date(start));
            filters.put("byStart", p);
        }
        if (due != null) {
            p.and("due", new Date(due));
            filters.put("byDue", p);
        }

        return filters;
    }
}
