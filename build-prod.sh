#!/bin/bash

# ========================================
# 운영환경 빌드 스크립트
# ========================================

echo "🏗️ 운영환경용 JAR 파일을 빌드합니다..."

# 운영환경 프로파일로 빌드
./gradlew clean build -Pprod

echo "✅ 운영환경 빌드가 완료되었습니다."
echo "📦 JAR 파일 위치: build/libs/hdds-ch-happs-btch-0.0.1-SNAPSHOT.jar" 