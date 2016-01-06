 (ns journalist.publications)

 (def outlets '[
               {:slug "new-statesman"
                :rss-feed "journalist.new_statesman.rss_reader/process_rss_feed"}
               {:slug "the-independent"
                :rss-feed "journalist.the_independent.rss_reader/process-rss-feed"}
               ])
