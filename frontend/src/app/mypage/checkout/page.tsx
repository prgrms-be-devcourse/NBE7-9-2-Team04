"use client"

//결제 성공
import { loadTossPayments, ANONYMOUS } from "@tosspayments/tosspayments-sdk";
import { useEffect, useState } from "react";

const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";
const customerKey = generateRandomString();

export default function CheckoutPage() {
  const [amount, setAmount] = useState({
    currency: "KRW",
    value: 50000,
  });
  const [ready, setReady] = useState(false);
  const [widgets, setWidgets] = useState<any>(null);

  useEffect(() => {
    async function fetchPaymentWidgets() {
      try {
        // ------  SDK 초기화 ------
        const tossPayments = await loadTossPayments(clientKey);

        // 회원 결제
        const widgets = tossPayments.widgets({customerKey,});

        setWidgets(widgets);
      } catch (error) {
        console.error("Error fetching payment widget:", error);
      }
    }

    fetchPaymentWidgets();
  }, [clientKey, customerKey]);

  useEffect(() => {
    async function renderPaymentWidgets() {
      if (widgets == null) {
        return;
      }

      // ------  주문서의 결제 금액 설정 ------
      await widgets.setAmount(amount);

      // ------  결제 UI 렌더링 ------
      await widgets.renderPaymentMethods({
        selector: "#payment-method",
        // 렌더링하고 싶은 결제 UI의 variantKey
        variantKey: "DEFAULT",
      });

      // ------  이용약관 UI 렌더링 ------
      await widgets.renderAgreement({
        selector: "#agreement",
        variantKey: "AGREEMENT",
      });

      setReady(true);
    }

    renderPaymentWidgets();
  }, [widgets]);

  const updateAmount = async (amount: any) => {
    setAmount(amount);
    await widgets.setAmount(amount);
  };

    // 결제 요청 처리
   const handlePayment = async () => {
        if (!widgets) return;
    
        try {
          await widgets.requestPayment({
            orderId: generateRandomString(),
            orderName: "PREMIUM 구독 결제",
            successUrl: `${window.location.origin}/mypage/success`,
            failUrl: `${window.location.origin}/mypage/fail`,
            customerEmail: "customer123@gmail.com",
            customerName: "홍길동",
            customerMobilePhone: "01012341234",
          });
        } catch (error) {
          console.error("결제 요청 중 오류:", error);
        }
      
    };

  return (
    <div className="wrapper">
      <div className="box_section">
        {/* 결제 UI */}
        <div id="payment-method" />
        {/* 이용약관 UI */}
        <div id="agreement" />
        {/* 쿠폰 체크박스 */}
        <div style={{ paddingLeft: "24px" }}>
          <div className="checkable typography--p">
            <label
              htmlFor="coupon-box"
              className="checkable__label typography--regular"
            >
              <input
                id="coupon-box"
                className="checkable__input"
                type="checkbox"
                aria-checked="true"
                disabled={!ready}
                // ------  주문서의 결제 금액이 변경되었을 경우 결제 금액 업데이트 ------
                onChange={async (event) => {
                  await updateAmount({
                    currency: amount.currency,
                    value: event.target.checked
                      ? amount.value - 5000
                      : amount.value + 5000,
                  });
                }}
              />
              <span className="checkable__label-text">5,000원 쿠폰 적용</span>
            </label>
          </div>
        </div>

        {/* 결제하기 버튼 */}
        <button
          className="button"
          style={{ marginTop: "30px" }}
          disabled={!ready}
          // ------ '결제하기' 버튼 누르면 결제창 띄우기 ------
          onClick={async () => {
            try {
              await widgets.requestPayment({
                orderId: generateRandomString(),
                orderName: "토스 티셔츠 외 2건",
                successUrl: window.location.origin + "/mypage/success",
                failUrl: window.location.origin + "/mypage/fail",
                customerEmail: "customer123@gmail.com",
                customerName: "김토스",
                customerMobilePhone: "01012341234",
              });
            } catch (error) {
              console.error(error);
            }
          }}
        >
          결제하기
        </button>
      </div>
    </div>
  );
}

function generateRandomString() {
  return window.btoa(Math.random().toString()).slice(0, 20);
}
