 (ns journalist.publications)

 (def outlets '[
               {:slug "new-statesman"
                :rss-feed "journalist.new_statesman.rss_reader/process_rss_feed"}
               {:slug "evening-standard"
           		:rss-feed "journalist.evening_standard.rss_reader/process_rss_feed"}
               ])
