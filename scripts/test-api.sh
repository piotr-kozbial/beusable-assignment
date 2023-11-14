#!/bin/sh

URL='http://localhost:8080/optimize-bookings'
REQUEST='{"freePremiumRooms":3, "freeEconomyRooms":3, "clientOffers": [23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]}'
EXPECTED_RESULT='{"premium":{"bookedRooms":3,"totalIncome":{"euros":738,"cents":0}},"economy":{"bookedRooms":3,"totalIncome":{"euros":167,"cents":99}}}'

ACTUAL_RESULT=`curl -q -d "$REQUEST" -H "Content-Type: application/json" -X POST "$URL"`

echo
if [ "${EXPECTED_RESULT}" = "${ACTUAL_RESULT}" ]; then
    echo "test passed"
else
    echo "TEST FAILED"
    echo "Actual result:"
    echo "${ACTUAL_RESULT}"
    exit 1
fi
echo
