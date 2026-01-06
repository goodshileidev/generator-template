#!/bin/bash
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT="${SCRIPT_DIR}"
cd "$ROOT/backend-template/be-parent"
mvn -B clean package
cd "$ROOT/backend-template/be-backend"
java -Dloader.main=com.partner.be.backend.GeneratorApplication -jar target/be-backend-1.0.0-SNAPSHOT.jar --spring.profiles.active=local
