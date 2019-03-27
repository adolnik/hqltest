set hive.exec.compress.intermediate=true;
set hive.exec.compress.output=true;
set mapred.output.compression.type=BLOCK;
set mapred.output.compression.codec=org.apache.hadoop.io.compress.SnappyCodec;
set hive.parquet.compression=SNAPPY;

set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;
set hive.exec.max.dynamic.partitions = 10000;
set hive.exec.max.dynamic.partitions.pernode=10000;
set hive.support.quoted.identifiers=none;

use ${dmDatabase};

CREATE TABLE IF NOT EXISTS ${dmDatabase}.mrr_sales_daily
(
  customer_id INT,
  total INT
)
STORED AS PARQUET;

TRUNCATE TABLE ${dmDatabase}.mrr_sales_daily;

WITH subs AS (
SELECT
    s.id,
    s.sales_order_id,
    s.customer_id,
    s.ad_id,
    s.stopped_at,
    s.expires_at,
    s.created_at,
    s.backorder_id,
    s.expired_reason,
    s.remnant,
    s.lock_version,
    s.quote_id,
    s.expired_by,
    sp.promo_id,
    sp.unit_price,
    sp.starts_on,
    sp.block_count,
    (sp.unit_price*sp.block_count)/100 as total,
    concat(s.id,"-",s.sales_order_id) as external_id
FROM ${srcMgdDatabase}.mrr_sales_subscription s
JOIN ${srcMgdDatabase}.mrr_sales_subscription_price sp ON s.id = sp.subscription_id
)
INSERT INTO ${dmDatabase}.mrr_sales_daily
SELECT
  x.customer_id,
  SUM(x.total) as total
FROM (
  SELECT
    subs.*,
    MAX(subs.starts_on) OVER (PARTITION BY subs.external_id) as max_starts_on
  FROM subs
) x
WHERE
  x.max_starts_on = x.starts_on
GROUP BY
  x.customer_id;
