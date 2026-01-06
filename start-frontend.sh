#!/bin/bash
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT="${SCRIPT_DIR}"
cd "$ROOT/frontend-template"
if [ ! -d node_modules ] || [ ! -f pnpm-lock.yaml ] ; then
  npm install --legacy-peer-deps
fi
MODE="${1:-start}"
case "$MODE" in
  start)
    npm run start
    ;;
  build)
    npm run build
    ;;
  *)
    echo "Usage: $0 [start|build]" >&2
    exit 1
    ;;
esac
