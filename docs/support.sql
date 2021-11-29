CTRL + F7

SELECT
    id,
    order_code,
    client_user_id,
    client_user_name,
    technician_user_id,
    technician_user_name,
    service_parts_required,
    service_parts_required_acknlg
FROM
    service_orders
WHERE
    order_code = 'W-1051';





SELECT
    id,
    order_code,
    service_items_code,
    service_items_name,
    client_user_id,
    client_user_name,
    technician_user_id,
    technician_user_name,
    is_schedule_order,
    schedule_date,
    service_parts_required,
    service_parts_required_acknlg
FROM
    service_orders
ORDER BY id asc;


SELECT
    id,
    order_code,
    service_items_code,
    service_items_name,
    client_user_id,
    client_user_name,
    technician_user_id,
    technician_user_name,
    is_schedule_order,
    schedule_date,
    service_parts_required,
    service_parts_required_acknlg
FROM
    service_orders
WHERE
    is_schedule_order IS NULL;




UPDATE service_orders
SET
    is_schedule_order = 0
WHERE
    is_schedule_order IS NULL;
COMMIT;


UPDATE "THIKTHAK"."SERVICE_ORDERS" SET IS_SCHEDULE_ORDER = '1' WHERE ROWID = 'AAAYBlAAEAAAAcPAAC' AND ORA_ROWSCN = '6035310903504'
