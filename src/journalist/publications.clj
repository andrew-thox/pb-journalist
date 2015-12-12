 (ns journalist.publications)

 (def outlets '[
               {:slug "new-statesman"
                :rss-feed "journalist.new_statesman.rss_reader/process_rss_feed"
                :archive "journalist.new_statesman.archive/process-archive"}
               {:slug "guardian" :rss-feed "whatever"}
               ])
