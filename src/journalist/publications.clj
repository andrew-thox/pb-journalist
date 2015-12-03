 (ns journalist.publications)

 (def outlets '[
               {:slug "new-statesman"
                :rss-feed "paperboy.new_statesman.rss_reader/process_rss_feed"
                :archive "paperboy.new_statesman.archive/process-archive"}
               {:slug "guardian" :rss-feed "whatever"}
               ])
