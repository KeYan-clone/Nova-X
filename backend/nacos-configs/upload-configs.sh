#!/bin/bash
# ========================================
# Nacos 配置自动上传脚本 (Linux/Mac)
# ========================================

NACOS_ADDR=${1:-"localhost:8848"}
NAMESPACE=${2:-"dev"}
USERNAME=${3:-"nacos"}
PASSWORD=${4:-"nacos"}

echo "========================================"
echo "  Nacos 配置上传工具"
echo "========================================"
echo ""

NACOS_URL="http://$NACOS_ADDR/nacos/v1/cs/configs"
CONFIG_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "配置目录: $CONFIG_DIR"
echo "Nacos 地址: $NACOS_ADDR"
echo "命名空间: $NAMESPACE"
echo ""

# 查找所有 yaml 文件
CONFIG_FILES=$(find "$CONFIG_DIR" -maxdepth 1 -name "*.yaml" -type f)

if [ -z "$CONFIG_FILES" ]; then
    echo "❌ 未找到配置文件"
    exit 1
fi

echo "找到以下配置文件:"
echo "$CONFIG_FILES" | while read file; do
    echo "  - $(basename "$file")"
done
echo ""

# 上传配置
SUCCESS_COUNT=0
FAIL_COUNT=0

echo "$CONFIG_FILES" | while read file; do
    DATA_ID=$(basename "$file")
    CONTENT=$(cat "$file")

    echo -n "正在上传: $DATA_ID ... "

    RESPONSE=$(curl -s -X POST "$NACOS_URL" \
        -d "dataId=$DATA_ID" \
        -d "group=DEFAULT_GROUP" \
        -d "tenant=$NAMESPACE" \
        -d "content=$CONTENT" \
        -d "type=yaml")

    if [ "$RESPONSE" == "true" ]; then
        echo "✅ 成功"
        ((SUCCESS_COUNT++))
    else
        echo "❌ 失败"
        ((FAIL_COUNT++))
    fi
done

echo ""
echo "========================================"
echo "上传完成！"
echo "========================================"
echo ""
echo "💡 提示："
echo "  1. 访问 Nacos 控制台: http://$NACOS_ADDR/nacos"
echo "  2. 在 配置管理 > 配置列表 中查看已上传的配置"
echo "  3. 根据需要修改配置中的 MySQL、Redis、Kafka 地址"
echo ""
